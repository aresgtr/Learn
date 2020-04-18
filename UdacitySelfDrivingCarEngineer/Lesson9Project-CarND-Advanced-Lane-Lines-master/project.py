import cv2
import glob
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import ntpath
import numpy as np
import os


def main():
    # # Make a list of calibration images
    # images = glob.glob('camera_cal/calibration*.jpg')
    # objpoints, imgpoints = process_images_for_calibration(images, 9, 6)
    #
    # # Retrieve image size for calibration
    # img = cv2.imread('camera_cal/calibration1.jpg')
    # img_size = (img.shape[1], img.shape[0])
    #
    # # Do camera calibration given object points and image points
    # ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, img_size, None, None)
    #
    # for idx, fname in enumerate(images):
    #     image = correct_image_distortion(fname, mtx, dist)
    #     save_image(fname, image, 'camera_cal_out/')
    #
    # # TODO: Below are after camera calibration
    #
    # images = glob.glob('test_images/*')
    # for idx, fname in enumerate(images):
    #     image = correct_image_distortion(fname, mtx, dist)
    #     image = create_thresholded_binary_image(image)
    #     # image = perspective_transform(image)
    #
    #     # plt.imshow(img, cmap='gray')
    #     save_image(fname, image, 'test_images_out_temp/')

    # For Testing
    image = cv2.imread('test_images_out_temp/test3.jpg')
    image = perspective_transform(image)
    image = fit_polynomial(image)
    image = reverse_perspective_transform(image)
    # plt.imshow(image)


def process_images_for_calibration(images, inner_x, inner_y):
    objpoints = []  # 3D poi nts in real world space
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

            # draw and display the corners
            # image = cv2.drawChessboardCorners(image, (inner_x, inner_y), corners, ret)  # TODO: remove
            # plt.imshow(image)

    return objpoints, imgpoints


def correct_image_distortion(fname, mtx, dist):
    image = cv2.imread(fname)
    image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)  # Convert back to RGB

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
    # Stack each channel
    # color_binary = np.dstack((np.zeros_like(sxbinary), sxbinary, s_binary)) * 255 # TODO remove
    combined_binary = np.zeros_like(sxbinary)
    combined_binary[(s_binary == 1) | (sxbinary == 1)] = 1
    return combined_binary


def perspective_transform(image):
    x_len = image.shape[1]
    y_len = image.shape[0]
    # plt.plot(150, y_len, 'x')  # bottom left                    # TODO: remove
    # plt.plot(30 + x_len - 150, y_len, 'x')  # bottom right
    # plt.plot(160 + 410, y_len * 63 / 100, 'x')  # top left
    # plt.plot(30 + x_len - 160 - 410, y_len * 63 / 100, 'x')  # top right

    image_size = (image.shape[1], image.shape[0])

    # Four source coordinates
    src = np.float32(
        [[150, y_len],
         [30 + x_len - 150, y_len],
         [160 + 420, y_len * 63 / 100],  # Top left
         [30 + x_len - 160 - 450, y_len * 63 / 100]])  # Top right

    # Four desired coordinates
    dst = np.float32(
        [[150, y_len],
         [30 + x_len - 150, y_len],
         [150, 0],
         [30 + x_len - 150, 0]])

    M = cv2.getPerspectiveTransform(src, dst)
    warped = cv2.warpPerspective(image, M, image_size, flags=cv2.INTER_NEAREST)  # keep same size as input image

    return warped


def reverse_perspective_transform(image):
    x_len = image.shape[1]
    y_len = image.shape[0]

    image_size = (image.shape[1], image.shape[0])

    # Four source coordinates
    src = np.float32(
        [[150, y_len],
         [30 + x_len - 150, y_len],
         [150, 0],
         [30 + x_len - 150, 0]])

    # Four desired coordinates
    dst = np.float32(
        [[150, y_len],
         [30 + x_len - 150, y_len],
         [160 + 420, y_len * 63 / 100],  # Top left
         [30 + x_len - 160 - 450, y_len * 63 / 100]])  # Top right

    M = cv2.getPerspectiveTransform(src, dst)
    warped = cv2.warpPerspective(image, M, image_size, flags=cv2.INTER_NEAREST)  # keep same size as input image

    return warped


def find_lane_pixels(image):

    gray = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)
    ret, binary_warped = cv2.threshold(gray, 127, 255, cv2.THRESH_BINARY)

    # Normalize image from 0-1
    binary_warped = binary_warped / 255

    histogram = np.sum(binary_warped[binary_warped.shape[0] // 2:, :], axis=0)
    # plt.plot(histogram)       #   TODO delete
    # Create an output image to draw on and visualize the result
    out_img = np.dstack((binary_warped, binary_warped, binary_warped))

    # Find the peak of the left and right halves of the histogram
    # These will be the starting point for the left and right lines
    midpoint = np.int(histogram.shape[0] // 2)
    leftx_base = np.argmax(histogram[:midpoint])
    rightx_base = np.argmax(histogram[midpoint:]) + midpoint

    # HYPERPARAMETERS
    # Choose the number of sliding windows
    nwindows = 9
    # Set the width of the windows +/- margin
    margin = 100
    # Set minimum number of pixels found to recenter window
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

        # Draw the windows on the visualization image
        # cv2.rectangle(out_img, (win_xleft_low, win_y_low),
        #               (win_xleft_high, win_y_high), (0, 255, 0), 2)
        # cv2.rectangle(out_img, (win_xright_low, win_y_low),
        #               (win_xright_high, win_y_high), (0, 255, 0), 2)  # TODO delete

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

    return leftx, lefty, rightx, righty, out_img


def fit_polynomial(binary_warped):
    # Find our lane pixels first
    leftx, lefty, rightx, righty, out_img = find_lane_pixels(binary_warped)

    # Fit a second order polynomial to each using `np.polyfit`
    left_fit = np.polyfit(lefty, leftx, 2)
    right_fit = np.polyfit(righty, rightx, 2)

    # Generate x and y values for plotting
    ploty = np.linspace(0, binary_warped.shape[0]-1, binary_warped.shape[0] )
    try:
        left_fitx = left_fit[0]*ploty**2 + left_fit[1]*ploty + left_fit[2]
        right_fitx = right_fit[0]*ploty**2 + right_fit[1]*ploty + right_fit[2]
    except TypeError:
        # Avoids an error if `left` and `right_fit` are still none or incorrect
        print('The function failed to fit a line!')
        left_fitx = 1*ploty**2 + 1*ploty
        right_fitx = 1*ploty**2 + 1*ploty

    ## Visualization ##
    # Colors in the left and right lane regions
    # out_img[lefty, leftx] = [255, 0, 0]
    # out_img[righty, rightx] = [0, 0, 255] # TODO delete

    # plt.plot((right_fitx - left_fitx) / 2 + left_fitx , ploty, color = 'green', linewidth = 240, alpha = 0.5)  # TODO linewidth dynamically adjusted by width
    # # Plots the left and right polynomials on the lane lines
    # plt.plot(left_fitx, ploty, color='yellow')
    # plt.plot(right_fitx, ploty, color='yellow')

    out_img = create_new_path_img(out_img, left_fitx, right_fitx, ploty)

    return out_img


def create_new_path_img(out_img, left_fitx, right_fitx, ploty):
    path_image = np.zeros((out_img.shape[0], out_img.shape[1]), np.uint8)

    plt.plot((right_fitx - left_fitx) / 2 + left_fitx, ploty, color='green', linewidth=240,
             alpha=0.5)  # TODO linewidth dynamically adjusted by width
    # Plots the left and right polynomials on the lane lines
    plt.plot(left_fitx, ploty, color='yellow')
    plt.plot(right_fitx, ploty, color='yellow')
    return path_image


def save_image(fname, image, output_dir):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # Save the output image
    plt.imsave(output_dir + ntpath.basename(fname), image)


if __name__ == '__main__':
    main()
    plt.show()
