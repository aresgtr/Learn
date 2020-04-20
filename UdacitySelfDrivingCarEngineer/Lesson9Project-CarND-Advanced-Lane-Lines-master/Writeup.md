## Project: Advanced Lane Finding

**Advanced Lane Finding Project**

The steps of this project are the following:

* Compute the camera calibration matrix and distortion coefficients given a set of chessboard images.
* Apply a distortion correction to raw images.
* Use color transforms, gradients, etc., to create a thresholded binary image.
* Apply a perspective transform to rectify binary image ("birds-eye view").
* Detect lane pixels and fit to find the lane boundary.
* Determine the curvature of the lane and vehicle position with respect to center.
* Warp the detected lane boundaries back onto the original image.
* Output visual display of the lane boundaries and numerical estimation of lane curvature and vehicle position.
* Use this methodology to analyze each frame of a video from a car dash cam 

[//]: # (Image References)

[image1]: ./examples/undistort_output.png "Undistorted"
[image2]: ./test_images/test1.jpg "Road"
[image2_undist]: ./test_images_undist/test1.jpg "Undistorted Road"
[image3]: ./test_images_thresholded_binary/test1.jpg "Binary Example"
[image_perspective_transform]: ./test_images_perspective_transform/test1.jpg "Perspective Transform"
[image4]: ./examples/warped_straight_lines.jpg "Warp Example"
[image_perspective_transform_straight]: ./test_images_perspective_transform/straight_lines1.jpg "Perspective Transform Straight"
[image5]: ./examples/color_fit_lines.jpg "Fit Visual"
[image6]: ./output_images/test1.jpg "Output"
[video1]: ./project_video.mp4 "Video"
[undistorted_calibration_image]: ./camera_cal_out/calibration13.jpg "Undistorted Calibration Image"



### Camera Calibration

#### 1. Process calibration images prior to calibration

```python
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
```  

I start by preparing "object points", which will be the (x, y, z) coordinates of the chessboard corners in the real world. Here I am assuming the chessboard is fixed on the (x, y) plane at z=0, such that the object points are the same for each calibration image.  Thus, `objp` is just a replicated array of coordinates, and `objpoints` will be appended with a copy of it every time I successfully detect all chessboard corners in a test image.  `imgpoints` will be appended with the (x, y) pixel position of each of the corners in the image plane with each successful chessboard detection.

`inner_x`: number of inner corners in the x direction

`inner_y`: number of inner corners in the y direction

#### 2. Calibrate Camera

```python
# Retrieve image size for calibration
img = cv2.imread(images[0])
img_size = (img.shape[1], img.shape[0])

# Do camera calibration given object points and image points
ret, mtx, dist, rvecs, tvecs = cv2.calibrateCamera(objpoints, imgpoints, img_size, None, None)

# Save the camera calibration result for later use
dist_pickle = {}
dist_pickle["mtx"] = mtx
dist_pickle["dist"] = dist
pickle.dump(dist_pickle, open("wide_dist_pickle.p", "wb"))
```

I then used the output `objpoints` and `imgpoints` to compute the camera calibration and distortion coefficients using the `cv2.calibrateCamera()` function. The coefficients are saved in a pickle file for later use.

#### 3. Undistort Calibration Images

I applied this distortion correction to the calibration image using the `cv2.undistort()` function and obtained results like this: 

![alt text][image1]

More undistorted calibration images:

![alt text][undistorted_calibration_image]


### Pipeline (single images)

#### 1. An example of a distortion-corrected image.

In the first step, distortion correction is applied to test images and video frames like this one below.

Before distortion correction:

![alt text][image2]

After distortion correction:

![alt text][image2_undist]

#### 2. Create thresholded binary image

I used a combination of color and gradient thresholds to generate a binary image.  Here's an example of my output for this step.

![alt text][image3]

```python
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
```

#### 3. Perspective transform

By selecting area of interest, perspective transform is applied to provide "birds-eye view" for the lane ahead of the camera. The following image is the perspective transform from the images above.

![alt text][image_perspective_transform]

`cv2.warpPerspective` is called for perspective transform. The function takes as inputs an image as well as transformation matrix. The transformation matrix is generated by cv2 from source and destination points. I chose the hardcode the source and destination points in the following manner:

```python
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
```

This resulted in the following source and destination points:

| Source        | Destination   | 
|:-------------:|:-------------:| 
| 0, 720        | 0, 720        | 
| 1280, 720     | 1280, 720     |
| 566, 453.6    | 0, 0          |
| 714, 453.6    | 1280, 0       |

I verified that my perspective transform was working as expected by drawing the `src` and `dst` points onto a test image and its warped counterpart to verify that the lines appear parallel in the warped image.

Example:

![alt text][image4]

My perspective transform image for calibration:

![alt text][image_perspective_transform_straight]

#### 4. Identified lane-line pixels and fit their positions with polynomials

Below image briefly explains how the pixels are detected and fitted by 2nd order polynomial.

![alt text][image5]

First, lane pixels are identified by `find_lane_pixels()`. The pixels on the bottom half of the perspective transform image is added for a histogram for identify the most possible lane positions.

```python
# Use histogram to detect the position of lanes by looking at the bottom half of the image
histogram = np.sum(binary_warped[binary_warped.shape[0] // 2:, :], axis=0)

# Find the peak of the left and right halves of the histogram
# These will be the starting point for the left and right lines
midpoint = np.int(histogram.shape[0] // 2)
leftx_base = np.argmax(histogram[:midpoint])
rightx_base = np.argmax(histogram[midpoint:]) + midpoint
```

Start from left_base and right_base, a sliding window is set for each of the lane. The sliding window move from bottom to the top of the image, adjusting its x values by centering itself to the pixels, and then the pixel coordinates are generated. The coordinates are in the form of numpy arrays, including leftx, lefty, rightx, righty, for each line's x and y coordinates.

The four coordinate values are used to determine the 2nd order polynomial for the lane lines.

```python
# Fit a second order polynomial to each
left_fit = np.polyfit(lefty, leftx, 2)
right_fit = np.polyfit(righty, rightx, 2)
```

Then the polynomial is transformed into x and y coordinates for analyzation and plotting

```python
ploty = np.linspace(0, binary_warped.shape[0] - 1, binary_warped.shape[0])

left_fitx = left_fit[0] * ploty ** 2 + left_fit[1] * ploty + left_fit[2]
right_fitx = right_fit[0] * ploty ** 2 + right_fit[1] * ploty + right_fit[2]
```

#### 5. Calculated the radius of curvature of the lane and the position of the vehicle with respect to center.

The similar methodology is used for calculate curvature. Please refer to `measure_curvature_and_position()` for detailed explainations.

Since the images are in the pixels, we need to transform them into meters as in the real world.

```python
# Define conversions in x and y from pixels space to meters
ym_per_pix = 30 / 720  # meters per pixel in y dimension
xm_per_pix = 3.7 / 700  # meters per pixel in x dimension
```

The multiplier above is used to compute real world polynomial, and then transform the polynomial into curvature.

```python
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
```

For vehicle position with respect to center, first we need `left_fitx` and `right_fitx` data calculated above for lane lines. By locating the two lines's center position at the bottom of the image with respect to the real center point at the bottom of the image, we can calculate the offset.

```python
length = binary_warped.shape[1]
vehicle_pos = length / 2
lane_center_pos = (right_fitx[len(right_fitx) - 1] - left_fitx[len(left_fitx) - 1]) / 2 + left_fitx[
    len(left_fitx) - 1]
return vehicle_pos - lane_center_pos  # offset in pixels
```

And then, by multiplying the offset with the real world multiplier, we can obtain the offset in meters.

#### 6. Provide an example image of your result plotted back down onto the road such that the lane area is identified clearly.

Finally, the lane image is plotted and converted back from inverse perspective transform. The lane image then is combined with the initial undistorted image. Curvature data and offset data is showing on the image. Here is an example of my result on a test image:

![alt text][image6]

---

### Pipeline (video)

#### 1. Provide a link to your final video output.  Your pipeline should perform reasonably well on the entire project video (wobbly lines are ok but no catastrophic failures that would cause the car to drive off the road!).

Here's a [link to my video result](./project_video.mp4)

---

### Discussion

#### 1. Briefly discuss any problems / issues you faced in your implementation of this project.  Where will your pipeline likely fail?  What could you do to make it more robust?

Here I'll talk about the approach I took, what techniques I used, what worked and why, where the pipeline might fail and how I might improve it if I were going to pursue this project further.  
