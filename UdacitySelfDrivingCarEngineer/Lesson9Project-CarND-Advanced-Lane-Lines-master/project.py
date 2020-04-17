import cv2
import glob
import matplotlib.pyplot as plt
import matplotlib.image as mpimg
import ntpath
import numpy as np
import os


def main():
    # Make a list of calibration images
    images = glob.glob('camera_cal/calibration*.jpg')
    objpoints, imgpoints = process_images_for_calibration(images, 9, 6)

    # Retrieve image size for calibration
    img = cv2.imread('camera_cal/calibration1.jpg')
    img_size = (img.shape[1], img.shape[0])

    # Do camera calibration given object points and image points
    ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, img_size, None, None)

    for idx, fname in enumerate(images):
        image = correct_image_distortion(fname, mtx, dist)
        save_image(fname, image, 'camera_cal_out/')

    # TODO: Below are after camera calibration

    images = glob.glob('test_images/*')
    for idx, fname in enumerate(images):
        image = correct_image_distortion(fname, mtx, dist)
        image = create_thresholded_binary_image(image)
        # plt.imshow(img, cmap='gray')
        save_image(fname, image, 'test_images_out_temp/')


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


def save_image(fname, image, output_dir):
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # Save the output image
    plt.imsave(output_dir + ntpath.basename(fname), image)


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


if __name__ == '__main__':
    main()
    plt.show()
