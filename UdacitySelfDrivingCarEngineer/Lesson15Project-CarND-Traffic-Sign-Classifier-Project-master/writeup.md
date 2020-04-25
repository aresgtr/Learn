# **Traffic Sign Recognition** 

## Writeup


---

**Build a Traffic Sign Recognition Project**

The goals / steps of this project are the following:
* Load the data set (see below for links to the project data set)
* Explore, summarize and visualize the data set
* Design, train and test a LeNet model architecture
* Use the model to make predictions on new images
* Analyze the softmax probabilities of the new images
* Summarize the results with a written report


[//]: # (Image References)

[image2]: ./writeupimage/gray.jpg "Grayscaling"
[image_sample]: ./writeupimage/sample_image.jpg "Sample Images"
[image1]: ./writeupimage/visualization.jpg "Visualization"
[image4]: ./trafficsigns/4.jpg "Traffic Sign 1"
[image5]: ./trafficsigns/14.jpg "Traffic Sign 2"
[image6]: ./trafficsigns/25.jpg "Traffic Sign 3"
[image7]: ./trafficsigns/33.jpg "Traffic Sign 4"
[image8]: ./trafficsigns/40.jpg "Traffic Sign 5"
[5_images]: ./writeupimage/5_images.jpg "5 Images"
---

Here is a link to my [project code](https://github.com/udacity/CarND-Traffic-Sign-Classifier-Project/blob/master/Traffic_Sign_Classifier.ipynb)

### Data Set Summary & Exploration

#### 1. A basic summary of the training dataset

I used the matplotlib and numpy library to calculate summary statistics of the traffic
signs data set:

* The size of training set is 34799
* The size of the validation set is 4410
* The size of test set is 12630
* The shape of a traffic sign image is (32, 32, 3)
* The number of unique classes/labels in the data set is 43

Sample images in the training set:

![alt text][image_sample]

#### 2. Exploratory visualization of the dataset.

Since we know we have 43 unique classes, a bar chart is suitable for visualizing how many training images are available for each class. If one unique class only have several images, it is not enough for training. In out training example, all classes have enough images for training.

![alt text][image1]

### Design and Test a Model Architecture

#### 1. Preprocessed the image data

First, test images are converted to grayscale. Grayscale images works well in image processing because it simplifies the classification algorithm.

Here is an example of a traffic sign image before and after grayscaling.

![alt text][image2]

Training images are normalized to [-1, 1] for better accuracy. 


#### 2. Final model architecture

My final model consisted of the following layers:

| Layer         		|     Description	        					| 
|:---------------------:|:---------------------------------------------:| 
| Input         		| 32x32x1 Grayscale image   					| 
| Convolution       	| 1x1 stride, valid padding, outputs 28x28x6 	|
| RELU					|												|
| Max pooling	      	| 2x2 stride,  outputs 14x14x6 				    |
| Convolution    	    | 1x1 stride, valid padding, outputs 10x10x16   |
| RELU					|												|
| Max pooling	      	| 2x2 stride,  outputs 5x5x16 				    |
| Flatten   	      	| outputs 400                				    |
| Dropout               |                                               |
| Fully connected		| inputs 400, outputs 120						|
| RELU					|												|
| Fully connected		| inputs 120, outputs 84						|
| RELU					|												|
| Fully connected		| inputs 84, outputs 43					    	|
| Softmax				|           									|
|						|												|
|						|												|
 


#### 3. Model Training

To train the model, I used a learning rate of 0.0009, slightly lower than the 0.001 starting point. I found that a slightly lower learning rate combined with higher number of Epochs is more time-consuming but gives better result. I set batch size 128 as default, and epochs to be 30. ADAM optimizer is chosen for better accuracy. 

#### 4. Approach taken for finding a solution

My final model results were:
* validation set accuracy of 0.946

Firstly I implemented the LeNet architecture provided by the previous lab. It is a well-done architectture, which gives me a validation accuracy around 0.89. The goal for this project is to improve the LeNet architecture for a better accuracy.

I put a dropout layer after the flatten layer. According to the lecture, dropout forces the network to learn redundant representations and increase the robustness. It helps to increase the training accuracy and avoid overfitting. After applying the dropout layer, accuracy is increased.

 

### Test a Model on New Images

#### 1. Choose five German traffic signs found on the web and provide them in the report.

Here are five German traffic signs that I found on the web:

![alt text][5_images]

They are various in size and brightness.

The '70' (fourth) image might be difficult to classify because it is slightly tilted to the right.

In order to apply the network to the images, they are converted to the size of (32x32) and to grayscale. Normalization is also applied.

```python
images = glob.glob('trafficsigns/*')

# Resize images to 32x32 Grayscale and append the X and y list
dsize = (32, 32)
X_real = []
y_real = []

for idx, fname in enumerate(images):
    image = cv2.imread(fname)
    image = cv2.cvtColor(image, cv2.COLOR_RGB2GRAY)
    resized = cv2.resize(image, dsize)
    X_real.append(resized)
    y_real.append(ntpath.basename(fname).split('.')[0])

X_real = np.expand_dims(X_real, -1)

# Normalize X_real
X_real = X_real / 127.5 - 1
```

#### 2. Model's predictions on these new traffic signs

Here are the results of the prediction:

| Image			        |     Prediction	        					| 
|:---------------------:|:---------------------------------------------:| 
| Speed limit (70km/h)  | Speed limit (30km/h)   						| 
| Stop     		    	| Stop 						    				|
| Road work				| Road work										|
| Turn right ahead	    | Turn right ahead				 				|
| Roundabout mandatory	| Roundabout mandatory      					|


The model was able to correctly guess 4 of the 5 traffic signs, which gives an accuracy of 80%, which is lower when comparing validating the model (95%).

#### 3. How certain the model is when predicting on each of the five new images by looking at the softmax probabilities for each prediction.

![alt text][image7]

For this image, the model is relatively sure that this is a Turn Right Ahead sign (probability of 0.52), and the image does contain a Turn Right Ahead sign. The top five soft max probabilities were

| Probability         	|     Prediction	        					| 
|:---------------------:|:---------------------------------------------:| 
| .52         			| Turn right ahead   							| 
| .28     				| Speed limit (30km/h) 							|
| .088					| Roundabout mandatory							|
| .032	      			| Bicycles crossing	    		 				|
| .016				    | Keep left         							|

---

![alt text][image6]

For this image, the model is very certain that this is a Road Work sign (probability of 0.99), and the image does contain a Turn Road Work sign. The top five soft max probabilities were

| Probability         	|     Prediction	        					| 
|:---------------------:|:---------------------------------------------:| 
| .99         			| Road work   					        		| 
| .0059    				| Bicycles crossing 							|
| .0020					| Wild animals crossing							|
| .00010	   			| Double curve	    		 		    		|
| .000063			    | Slippery road         						|

---

![alt text][image8]

For this image, the model is very certain that this is a Road Work sign (probability of 0.93), and the image does contain a Turn Road Work sign. The top five soft max probabilities were

| Probability         	|     Prediction	        					| 
|:---------------------:|:---------------------------------------------:| 
| .93         			| Roundabout mandatory   					    | 
| .034    				| Priority road 							    |
| .017					| Right-of-way at the next intersection			|
| .013	   			    | Speed limit (100km/h)	    		 		    |
| .0074			        | End of no passing by vehicles over 3.5 metric tons	|

---

![alt text][image4]

For this image, the model is sure that this is a speed limit, but it fails to detect it is Speed limit (70km/h) (probability of 0.0018). The top five soft max probabilities were

| Probability         	|     Prediction	        					| 
|:---------------------:|:---------------------------------------------:| 
| .998         			| Speed limit (30km/h)   						| 
| .0018    				| Speed limit (70km/h) [Correct Answer] 		|
| .0000040				| Turn right ahead								|
| .00000047	    		| Speed limit (60km/h)			 				|
| .00000018			    | Speed limit (20km/h) 							|

---

![alt text][image5]

 For this image, the model is very certain that this is a Stop sign (probability of 0.9999), and the image does contain a Stop sign. The top five soft max probabilities were

| Probability         	|     Prediction	        					| 
|:---------------------:|:---------------------------------------------:| 
| .9999         		| Stop   					                    | 
| .000053    			| Speed limit (50km/h) 						    |
| .000011				| Keep left			                            |
| .0000020	   		    | Speed limit (80km/h)	    		 		    |
| .00000091			    | Road work	                                    |
