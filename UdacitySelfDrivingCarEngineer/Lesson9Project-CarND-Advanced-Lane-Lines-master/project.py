import cv2
import glob
import numpy as np
import matplotlib.pyplot as plt
import matplotlib.image as mpimg

objpoints = []  # 3D points in real world space
imgpoints = []  # 2D points in image plane

image = cv2.imread('camera_cal/calibration16.jpg')


def calibrate_camera(image, inner_x, inner_y):
    gray = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)

    objp = np.zeros((inner_y * inner_x, 3), np.float32)
    objp[:, :2] = np.mgrid[0:inner_x, 0:inner_y].T.reshape(-1, 2)

    ret, corners = cv2.findChessboardCorners(gray, (inner_x, inner_y), None)
    if ret == True:
        imgpoints.append(corners)
        objpoints.append(objp)

        # draw and display the corners
        image = cv2.drawChessboardCorners(image, (inner_x, inner_y), corners, ret)  # TODO: remove
        plt.imshow(image)


# Make a list of calibration images
images = glob.glob('camera_cal/calibration*.jpg')

for idx, fname in enumerate(images):
    image = cv2.imread(fname)
    calibrate_camera(image, 9, 6)

img = cv2.imread('camera_cal/calibration1.jpg')
img_size = (img.shape[1], img.shape[0])

ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, img_size, None, None)
# TODO: 看看这几个数字和单测calibration1相差多少
print(ret)

plt.show()

# TODO: distortion correction to raw highway images
