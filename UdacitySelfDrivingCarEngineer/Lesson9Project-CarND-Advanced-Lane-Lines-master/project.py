import cv2
import glob
import matplotlib.pyplot as plt
from moviepy.editor import VideoFileClip
import ntpath
import numpy as np
import os
import pickle


# Class to receive the characteristics of each line detection
class Line:
    def __init__(self):
        # was the line detected in the last iteration?
        self.detected = False
        # polynomial coefficients for the most recent fit
        self.current_fit = [np.array([False])]
        # plotting x value for the most recent fit
        self.current_fitx = [np.array([False])]
        # radius of curvature of the line in some units
        self.radius_of_curvature = None
        # distance in meters of vehicle center from the line center (only store in left lane)
        self.vehicle_pos = None
        # x values for detected line pixels

    def get_current_fit(self):
        return self.current_fit


'''Global Variables'''
left_line = Line()
right_line = Line()
sanity_check_frame_counter = 0


def main():
    # Calibrate Camera
    calibrate_camera('camera_cal/calibration*.jpg')

    # Correct distortion for calibration images
    images = glob.glob('camera_cal/calibration*.jpg')
    for idx, fname in enumerate(images):
        image = cv2.imread(fname)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)  # Convert back to RGB (Only for images)
        image = correct_image_distortion(image)
        save_image(fname, image, 'camera_cal_out/')

    # Process test_images
    images = glob.glob('test_images/*')
    for idx, fname in enumerate(images):
        image = cv2.imread(fname)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)  # Convert back to RGB (Only for images)
        result = process_image(image)
        save_image(fname, result, 'output_images/')

    # Process video
    white_output = 'test_videos_output/project_video.mp4'

    if not os.path.exists('test_videos_output/'):
        os.makedirs('test_videos_output/')

    # To speed up the testing process you may want to try your pipeline on a shorter subclip of the video
    # To do so add .subclip(start_second,end_second) to the end of the line below
    # Where start_second and end_second are integer values representing the start and end of the subclip
    # You may also uncomment the following line for a subclip of the first 5 seconds
    # clip1 = VideoFileClip("project_video.mp4").subclip(0, 5)

    clip1 = VideoFileClip("project_video.mp4")
    white_clip = clip1.fl_image(process_video_frame)  # NOTE: this function expects color images!!
    white_clip.write_videofile(white_output, audio=False)


def process_image(image):
    undist_image = correct_image_distortion(image)
    thresholded_binary_image = create_thresholded_binary_image(undist_image)
    warped_image = perspective_transform(thresholded_binary_image)
    warped_lane, curvature, position = search_and_construct_lane(warped_image)
    lane_image = perspective_transform(warped_lane, inverse=True)
    final_image = combine(lane_image, undist_image)
    texted_final_image = write_text_on_image(final_image, curvature, position)
    return texted_final_image


def process_video_frame(image):
    undist_image = correct_image_distortion(image)
    thresholded_binary_image = create_thresholded_binary_image(undist_image)
    warped_image = perspective_transform(thresholded_binary_image)

    # If it couldn't pass sanity check for 5 frames, new search is needed
    if (left_line.detected == False) or (right_line.detected == False):
        warped_lane, curvature, position = search_and_construct_lane(warped_image)
    else:  # If last frame detected lines, search from last frame's parameters to save time
        warped_lane, curvature, position = search_and_construct_lane(warped_image, from_previous=True)

    lane_image = perspective_transform(warped_lane, inverse=True)
    final_image = combine(lane_image, undist_image)
    texted_final_image = write_text_on_image(final_image, curvature, position)
    return texted_final_image


def calibrate_camera(source_path):
    # Make a list of calibration images
    images = glob.glob(source_path)
    objpoints, imgpoints = process_images_for_calibration(images, 9, 6)

    # Retrieve image size for calibration
    img = cv2.imread(images[0])
    img_size = (img.shape[1], img.shape[0])

    # Do camera calibration given object points and image points
    ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, img_size, None, None)

    # Save the camera calibration result for later use (we won't worry about rvecs / tvecs)
    dist_pickle = {}
    dist_pickle["mtx"] = mtx
    dist_pickle["dist"] = dist
    pickle.dump(dist_pickle, open("wide_dist_pickle.p", "wb"))


def process_images_for_calibration(images, inner_x, inner_y):
    objpoints = []  # 3D points in real world space
    imgpoints = []  # 2D points in image plan

    for idx, fname in enumerate(images):
        image = cv2.imread(fname)
        gray = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)

        objp = np.zeros((inner_y * inner_x, 3), np.float32)
        objp[:, :2] = np.mgrid[0:inner_x, 0:inner_y].T.reshape(-1, 2)

        ret, corners = cv2.findChessboardCorners(gray, (inner_x, inner_y), None)

        if ret:
            imgpoints.append(corners)
            objpoints.append(objp)

    return objpoints, imgpoints


def correct_image_distortion(image):
    # Read in the saved calibration variables
    dist_pickle = pickle.load(open("wide_dist_pickle.p", "rb"))
    mtx = dist_pickle["mtx"]
    dist = dist_pickle["dist"]

    dst = cv2.undistort(image, mtx, dist, None, mtx)
    return dst


def create_thresholded_binary_image(image, s_thresh=(170, 255), sx_thresh=(45, 80)):
    image = np.copy(image)

    # Convert to HLS color space and separate the V channel
    hls = cv2.cvtColor(image, cv2.COLOR_RGB2HLS)
    l_channel = hls[:, :, 1]
    s_channel = hls[:, :, 2]

    # Sobel x
    sobelx = cv2.Sobel(l_channel, cv2.CV_64F, 1, 0)  # Take the derivative in x
    abs_sobelx = np.absolute(sobelx)  # Absolute x derivative to accentuate lines away from horizontal
    scaled_sobel = np.uint8(255 * abs_sobelx / np.max(abs_sobelx))

    # Threshold x gradient
    sxbinary = np.zeros_like(scaled_sobel)
    sxbinary[(scaled_sobel >= sx_thresh[0]) & (scaled_sobel <= sx_thresh[1])] = 1

    # Threshold color channel
    s_binary = np.zeros_like(s_channel)
    s_binary[(s_channel >= s_thresh[0]) & (s_channel <= s_thresh[1])] = 1

    # Combine each channel
    combined_binary = np.zeros_like(sxbinary)
    combined_binary[(s_binary == 1) | (sxbinary == 1)] = 1
    return combined_binary


def perspective_transform(image, inverse=False):
    x_len = image.shape[1]
    y_len = image.shape[0]

    image_size = (image.shape[1], image.shape[0])

    # Four source coordinates
    src = np.float32(
        [[0, y_len],  # Bottom left
         [x_len, y_len],  # Bottom right
         [566, y_len * 63 / 100],  # Top left
         [x_len - 566, y_len * 63 / 100]])  # Top right

    # Four desired coordinates
    dst = np.float32(
        [[0, y_len],
         [x_len, y_len],
         [0, 0],
         [x_len, 0]])

    if not inverse:
        M = cv2.getPerspectiveTransform(src, dst)
        result = cv2.warpPerspective(image, M, image_size, flags=cv2.INTER_NEAREST)  # keep same size as input image
    else:  # Inverse transform
        Minv = cv2.getPerspectiveTransform(dst, src)
        result = cv2.warpPerspective(image, Minv, image_size, flags=cv2.INTER_NEAREST)

    return result


def find_lane_pixels(binary_warped):
    # Use histogram to detect the position of lanes by looking at the bottom half of the image
    histogram = np.sum(binary_warped[binary_warped.shape[0] // 2:, :], axis=0)

    # Find the peak of the left and right halves of the histogram
    # These will be the starting point for the left and right lines
    midpoint = np.int(histogram.shape[0] // 2)
    leftx_base = np.argmax(histogram[:midpoint])
    rightx_base = np.argmax(histogram[midpoint:]) + midpoint

    # HYPERPARAMETERS
    # Number of sliding windows
    nwindows = 9
    # Width of the windows +/- margin
    margin = 100
    # Minimum number of pixels found to recenter window
    minpix = 50

    # Set height of windows - based on nwindows above and image shape
    window_height = np.int(binary_warped.shape[0] // nwindows)
    # Identify the x and y positions of all nonzero pixels in the image
    nonzero = binary_warped.nonzero()
    nonzeroy = np.array(nonzero[0])
    nonzerox = np.array(nonzero[1])
    # Current positions to be updated later for each window in nwindows
    leftx_current = leftx_base
    rightx_current = rightx_base

    # Create empty lists to receive left and right lane pixel indices
    left_lane_inds = []
    right_lane_inds = []

    # Step through the windows one by one
    for window in range(nwindows):
        # Identify window boundaries in x and y (and right and left)
        win_y_low = binary_warped.shape[0] - (window + 1) * window_height
        win_y_high = binary_warped.shape[0] - window * window_height
        win_xleft_low = leftx_current - margin
        win_xleft_high = leftx_current + margin
        win_xright_low = rightx_current - margin
        win_xright_high = rightx_current + margin

        # Identify the nonzero pixels in x and y within the window #
        good_left_inds = ((nonzeroy >= win_y_low) & (nonzeroy < win_y_high) &
                          (nonzerox >= win_xleft_low) & (nonzerox < win_xleft_high)).nonzero()[0]
        good_right_inds = ((nonzeroy >= win_y_low) & (nonzeroy < win_y_high) &
                           (nonzerox >= win_xright_low) & (nonzerox < win_xright_high)).nonzero()[0]

        # Append these indices to the lists
        left_lane_inds.append(good_left_inds)
        right_lane_inds.append(good_right_inds)

        # If you found > minpix pixels, recenter next window on their mean position
        if len(good_left_inds) > minpix:
            leftx_current = np.int(np.mean(nonzerox[good_left_inds]))
        if len(good_right_inds) > minpix:
            rightx_current = np.int(np.mean(nonzerox[good_right_inds]))

    # Concatenate the arrays of indices (previously was a list of lists of pixels)
    try:
        left_lane_inds = np.concatenate(left_lane_inds)
        right_lane_inds = np.concatenate(right_lane_inds)
    except ValueError:
        # Avoids an error if the above is not implemented fully
        pass

    # Extract left and right line pixel positions
    leftx = nonzerox[left_lane_inds]
    lefty = nonzeroy[left_lane_inds]
    rightx = nonzerox[right_lane_inds]
    righty = nonzeroy[right_lane_inds]

    return leftx, lefty, rightx, righty


def find_lane_pixels_from_prior(binary_warped):
    left_fit = left_line.get_current_fit()
    right_fit = right_line.get_current_fit()

    # HYPERPARAMETER
    # Choose the width of the margin around the previous polynomial to search
    margin = 100

    # Grab activated pixels
    nonzero = binary_warped.nonzero()
    nonzeroy = np.array(nonzero[0])
    nonzerox = np.array(nonzero[1])

    # Set the area of search based on activated x-values within the +/- margin of our polynomial function
    left_lane_inds = ((nonzerox > (left_fit[0] * (nonzeroy ** 2) + left_fit[1] * nonzeroy +
                                   left_fit[2] - margin)) & (nonzerox < (left_fit[0] * (nonzeroy ** 2) +
                                                                         left_fit[1] * nonzeroy + left_fit[
                                                                             2] + margin)))
    right_lane_inds = ((nonzerox > (right_fit[0] * (nonzeroy ** 2) + right_fit[1] * nonzeroy +
                                    right_fit[2] - margin)) & (nonzerox < (right_fit[0] * (nonzeroy ** 2) +
                                                                           right_fit[1] * nonzeroy + right_fit[
                                                                               2] + margin)))

    # Extract left and right line pixel positions
    leftx = nonzerox[left_lane_inds]
    lefty = nonzeroy[left_lane_inds]
    rightx = nonzerox[right_lane_inds]
    righty = nonzeroy[right_lane_inds]

    return leftx, lefty, rightx, righty


def search_and_construct_lane(binary_warped, from_previous=False):
    # Find lane pixels first
    if not from_previous:
        leftx, lefty, rightx, righty = find_lane_pixels(binary_warped)
    else:
        leftx, lefty, rightx, righty = find_lane_pixels_from_prior(binary_warped)

    # Fit a second order polynomial to each
    left_fit = np.polyfit(lefty, leftx, 2)
    right_fit = np.polyfit(righty, rightx, 2)

    # Generate x and y values for plotting
    ploty = np.linspace(0, binary_warped.shape[0] - 1, binary_warped.shape[0])
    try:
        left_fitx = left_fit[0] * ploty ** 2 + left_fit[1] * ploty + left_fit[2]
        right_fitx = right_fit[0] * ploty ** 2 + right_fit[1] * ploty + right_fit[2]
    except TypeError:
        # Avoids an error if `left` and `right_fit` are still none or incorrect
        print('The function failed to fit a line!')
        left_fitx = 1 * ploty ** 2 + 1 * ploty
        right_fitx = 1 * ploty ** 2 + 1 * ploty

    left_curverad, right_curverad, position = measure_curvature_and_position(binary_warped, leftx, rightx, ploty, lefty,
                                                                             righty, left_fitx, right_fitx)
    curvature = (left_curverad + right_curverad) / 2
    warped_lane = draw_lane(binary_warped, left_fitx, right_fitx, ploty)

    # Sanity Check only if it is searching from previous
    if from_previous:
        check_pass = perform_sanity_check(left_curverad, right_curverad, left_fitx, right_fitx, position)
    else:
        check_pass = True

    # Deal with line objects, only update if sanity check pass (or not checked)
    if check_pass:
        left_line.detected = True
        right_line.detected = True

        left_line.current_fit = left_fit
        right_line.current_fit = right_fit

        left_line.current_fitx = left_fitx
        right_line.current_fitx = right_fitx

        left_line.radius_of_curvature = left_curverad
        right_line.radius_of_curvature = right_curverad

        left_line.vehicle_pos = position

    return warped_lane, curvature, position


def perform_sanity_check(left_curvature, right_curvature, left_fitx, right_fitx, position):
    threshold = 0.2  # 20% similarity

    road_width_pixel = right_fitx[len(right_fitx) - 1] - left_fitx[len(left_fitx) - 1]
    road_width_pixel_from_last_frame = right_line.current_fitx[len(right_line.current_fitx) - 1] - \
                                       left_line.current_fitx[len(left_line.current_fitx) - 1]

    road_far_width_pixel = right_fitx[0] - left_fitx[0]

    # Check if left lane have similar curvature as last frame
    if abs((left_curvature - left_line.radius_of_curvature) / left_line.radius_of_curvature) > threshold:
        sanity_check_pass = False

    # Check if right lane have similar curvature as last frame
    elif abs((right_curvature - right_line.radius_of_curvature) / right_line.radius_of_curvature) > threshold:
        sanity_check_pass = False

    # Check if vehicle position relative to the lane center is similar as last frame
    elif abs((position - left_line.vehicle_pos) / left_line.vehicle_pos) > threshold:
        sanity_check_pass = False

    # Check if the lane widths are at similar length as last frame
    elif abs((road_width_pixel - road_width_pixel_from_last_frame) / road_width_pixel_from_last_frame) > threshold:
        sanity_check_pass = False

    # Check if the lane is roughly parallel
    elif abs((road_far_width_pixel - road_width_pixel) / road_width_pixel) > threshold:
        sanity_check_pass = False
    else:
        sanity_check_pass = True

    global sanity_check_frame_counter

    if sanity_check_pass:
        sanity_check_frame_counter = 0

    else:  # If sanity check fails, counter increases
        sanity_check_frame_counter += 1

    if sanity_check_frame_counter >= 5:  # Reset after 5 frames, and search from beginning
        sanity_check_frame_counter = 0
        left_line.detected = False
        right_line.detected = False

    return sanity_check_pass


def measure_position_to_center(left_fitx, right_fitx, binary_warped):
    length = binary_warped.shape[1]
    vehicle_pos = length / 2
    lane_center_pos = (right_fitx[len(right_fitx) - 1] - left_fitx[len(left_fitx) - 1]) / 2 + left_fitx[
        len(left_fitx) - 1]
    return vehicle_pos - lane_center_pos


def measure_curvature_and_position(binary_warped, leftx, rightx, ploty, lefty, righty, left_fitx, right_fitx):
    """
    Calculates the curvature of polynomial functions in meters.
    """

    # Define conversions in x and y from pixels space to meters
    ym_per_pix = 30 / 720  # meters per pixel in y dimension
    xm_per_pix = 3.7 / 700  # meters per pixel in x dimension

    # Fit new polynomials to x,y in world space
    left_fit_cr = np.polyfit(lefty * ym_per_pix, leftx * xm_per_pix, 2)
    right_fit_cr = np.polyfit(righty * ym_per_pix, rightx * xm_per_pix, 2)

    # Define y-value where we want radius of curvature
    # We'll choose the maximum y-value, corresponding to the bottom of the image
    y_eval = np.max(ploty)

    # Calculation of R_curve (radius of curvature)
    left_curverad = ((1 + (2 * left_fit_cr[0] * y_eval * ym_per_pix + left_fit_cr[1]) ** 2) ** 1.5) / np.absolute(
        2 * left_fit_cr[0])
    right_curverad = ((1 + (2 * right_fit_cr[0] * y_eval * ym_per_pix + right_fit_cr[1]) ** 2) ** 1.5) / np.absolute(
        2 * right_fit_cr[0])

    position_to_center = measure_position_to_center(left_fitx, right_fitx, binary_warped)
    real_position_to_center = position_to_center * xm_per_pix

    return left_curverad, right_curverad, real_position_to_center


def draw_lane(warped, left_fitx, right_fitx, ploty):
    # Create an image to draw the lines on
    warp_zero = np.zeros_like(warped).astype(np.uint8)
    color_warp = np.dstack((warp_zero, warp_zero, warp_zero))

    # Recast the x and y points into usable format for cv2.fillPoly()
    pts_left = np.array([np.transpose(np.vstack([left_fitx, ploty]))])
    pts_right = np.array([np.flipud(np.transpose(np.vstack([right_fitx, ploty])))])
    pts = np.hstack((pts_left, pts_right))

    # Draw the lane onto the warped blank image
    cv2.fillPoly(color_warp, np.int_([pts]), (0, 255, 0))

    return color_warp


def combine(lane_image, image):
    return cv2.addWeighted(image, 1, lane_image, 0.3, 0)


def write_text_on_image(image, curvature, position):
    cur_text = 'Radius of Curvature = ' + str(int(curvature)) + '(m)'

    font = cv2.FONT_HERSHEY_SIMPLEX
    topPosition = (100, 80)
    fontScale = 2
    fontColor = (255, 255, 255)
    lineType = 2

    cv2.putText(image, cur_text,
                topPosition,
                font,
                fontScale,
                fontColor,
                lineType)

    if position >= 0:
        left_or_right = 'right'
    else:
        left_or_right = 'left'

    pos_text = 'Vehicle is ' + "{:.2f}".format(abs(position)) + 'm ' + left_or_right + ' of center'
    topPosition = (100, 150)

    cv2.putText(image, pos_text,
                topPosition,
                font,
                fontScale,
                fontColor,
                lineType)

    return image


def save_image(fname, image, output_dir):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)
    plt.imsave(output_dir + ntpath.basename(fname), image)


if __name__ == '__main__':
    main()
