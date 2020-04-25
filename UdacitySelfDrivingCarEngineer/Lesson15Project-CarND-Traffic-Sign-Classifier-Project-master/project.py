import glob
import ntpath
import pickle
import tensorflow as tf
from tensorflow.contrib.layers import flatten
import cv2
from sklearn.utils import shuffle
import numpy as np
import matplotlib.pyplot as plt


def LeNet(x):
    # Arguments used for tf.truncated_normal, randomly defines variables for the weights and biases for each layer
    mu = 0
    sigma = 0.1

    # Layer 1: Convolutional. Input = 32x32x1. Output = 28x28x6.
    conv1_W = tf.Variable(tf.truncated_normal(shape=(5, 5, 1, 6), mean=mu, stddev=sigma))
    conv1_b = tf.Variable(tf.zeros(6))
    conv1 = tf.nn.conv2d(x, conv1_W, strides=[1, 1, 1, 1], padding='VALID') + conv1_b

    # Activation.
    conv1 = tf.nn.relu(conv1)

    # Pooling. Input = 28x28x6. Output = 14x14x6.
    conv1 = tf.nn.max_pool(conv1, ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1], padding='VALID')

    # Layer 2: Convolutional. Output = 10x10x16.
    conv2_W = tf.Variable(tf.truncated_normal(shape=(5, 5, 6, 16), mean=mu, stddev=sigma))
    conv2_b = tf.Variable(tf.zeros(16))
    conv2 = tf.nn.conv2d(conv1, conv2_W, strides=[1, 1, 1, 1], padding='VALID') + conv2_b

    # Activation.
    conv2 = tf.nn.relu(conv2)

    # Pooling. Input = 10x10x16. Output = 5x5x16.
    conv2 = tf.nn.max_pool(conv2, ksize=[1, 2, 2, 1], strides=[1, 2, 2, 1], padding='VALID')

    # Flatten. Input = 5x5x16. Output = 400.
    fc0 = flatten(conv2)

    # Dropout
    fc0 = tf.nn.dropout(fc0, keep_prob)

    # Layer 3: Fully Connected. Input = 400. Output = 120.
    fc1_W = tf.Variable(tf.truncated_normal(shape=(400, 120), mean=mu, stddev=sigma))
    fc1_b = tf.Variable(tf.zeros(120))
    fc1 = tf.matmul(fc0, fc1_W) + fc1_b

    # Activation.
    fc1 = tf.nn.relu(fc1)

    # Layer 4: Fully Connected. Input = 120. Output = 84.
    fc2_W = tf.Variable(tf.truncated_normal(shape=(120, 84), mean=mu, stddev=sigma))
    fc2_b = tf.Variable(tf.zeros(84))
    fc2 = tf.matmul(fc1, fc2_W) + fc2_b

    # Activation.
    fc2 = tf.nn.relu(fc2)

    # Layer 5: Fully Connected. Input = 84. Output = 43.
    fc3_W = tf.Variable(tf.truncated_normal(shape=(84, 43), mean=mu, stddev=sigma))
    fc3_b = tf.Variable(tf.zeros(43))
    logits = tf.matmul(fc2, fc3_W) + fc3_b

    return logits


def evaluate(X_data, y_data):
    num_examples = len(X_data)
    total_accuracy = 0
    sess = tf.get_default_session()
    for offset in range(0, num_examples, BATCH_SIZE):
        batch_x, batch_y = X_data[offset:offset + BATCH_SIZE], y_data[offset:offset + BATCH_SIZE]
        accuracy = sess.run(accuracy_operation, feed_dict={x: batch_x, y: batch_y, keep_prob: 1.0})
        total_accuracy += (accuracy * len(batch_x))
    return total_accuracy / num_examples


def convert_images():
    images = glob.glob('trafficsigns/*')

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

    return X_real, y_real


"""
Step 0: Load the Data
"""

training_file = 'data/train.p'
validation_file = 'data/valid.p'
testing_file = 'data/test.p'

with open(training_file, mode='rb') as f:
    train = pickle.load(f)
with open(validation_file, mode='rb') as f:
    valid = pickle.load(f)
with open(testing_file, mode='rb') as f:
    test = pickle.load(f)

X_train, y_train = train['features'], train['labels']
X_valid, y_valid = valid['features'], valid['labels']
X_test, y_test = test['features'], test['labels']

"""
Step 1: Dataset Summary and Exploration
"""

### Replace each question mark with the appropriate value.
### Use python, pandas or numpy methods rather than hard coding the results

# TODO: Number of training examples
n_train = len(X_train)

# TODO: Number of validation examples
n_validation = len(X_valid)

# TODO: Number of testing examples.
n_test = len(X_test)

# TODO: What's the shape of an traffic sign image?
image_shape = X_train[0].shape

# TODO: How many unique classes/labels there are in the dataset.
n_classes = 0

for i in range(n_train):
    if y_train[i] > n_classes:
        n_classes = y_train[i]
n_classes = n_classes + 1

print("Number of training examples =", n_train)
print("Number of testing examples =", n_test)
print("Image data shape =", image_shape)
print("Number of classes =", n_classes)

"""
Pre-process Data
"""

# Validate data
assert (len(X_train) == len(y_train))
assert (len(X_valid) == len(y_valid))
assert (len(X_test) == len(y_test))

# Shuffle the training data
X_train, y_train = shuffle(X_train, y_train)

# To gray scale
X_train_grayscale = np.zeros(X_train.shape[:-1])
X_valid_grayscale = np.zeros(X_valid.shape[:-1])
X_test_grayscale = np.zeros(X_test.shape[:-1])

for i in range(X_train.shape[0]):
    X_train_grayscale[i] = cv2.cvtColor(X_train[i], cv2.COLOR_BGR2GRAY)

for i in range(X_valid.shape[0]):
    X_valid_grayscale[i] = cv2.cvtColor(X_valid[i], cv2.COLOR_BGR2GRAY)

for i in range(X_test.shape[0]):
    X_test_grayscale[i] = cv2.cvtColor(X_test[i], cv2.COLOR_BGR2GRAY)

# Plot the result
f, (ax1, ax2) = plt.subplots(1, 2, figsize=(24, 9))
f.tight_layout()
ax1.imshow(X_train[0])
ax1.set_title('Original Image', fontsize=50)
ax2.imshow(X_train_grayscale[0], cmap='gray')
ax2.set_title('Grayscale Image', fontsize=50)
plt.subplots_adjust(left=0., right=1, top=0.9, bottom=0.)

plt.show()

# Add last dimension after grayscale
# https://stackoverflow.com/questions/51872412/tensorflow-numpy-image-reshape-grayscale-images
X_train_grayscale = np.expand_dims(X_train_grayscale, -1)
X_valid_grayscale = np.expand_dims(X_valid_grayscale, -1)
X_test_grayscale = np.expand_dims(X_test_grayscale, -1)

X_train = X_train_grayscale
X_valid = X_valid_grayscale
X_test = X_test_grayscale

# X_train = np.sum(X_train / 3, axis=3, keepdims=True)
# X_test = np.sum(X_test / 3, axis=3, keepdims=True)
# X_valid = np.sum(X_valid / 3, axis=3, keepdims=True)

# Normalize
X_test = X_test / 127.5 - 1
X_train = X_train / 127.5 - 1
X_valid = X_valid / 127.5 - 1

"""
Model Architecture
"""

# Features and Labels
x = tf.placeholder(tf.float32, (None, 32, 32, 1))
y = tf.placeholder(tf.int32, (None))
one_hot_y = tf.one_hot(y, 43)

keep_prob = tf.placeholder(tf.float32)

# Training pipeline
EPOCHS = 30
BATCH_SIZE = 128
rate = 0.0009

logits = LeNet(x)

cross_entropy = tf.nn.softmax_cross_entropy_with_logits(labels=one_hot_y, logits=logits)
loss_operation = tf.reduce_mean(cross_entropy)
optimizer = tf.train.AdamOptimizer(learning_rate=rate)
training_operation = optimizer.minimize(loss_operation)

# Model Evaluation
correct_prediction = tf.equal(tf.argmax(logits, 1), tf.argmax(one_hot_y, 1))
accuracy_operation = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
saver = tf.train.Saver()

# Train the Model
# with tf.Session() as sess:
#     sess.run(tf.global_variables_initializer())
#     num_examples = len(X_train)
#
#     print("Training...")
#     print()
#     for i in range(EPOCHS):
#         X_train, y_train = shuffle(X_train, y_train)
#         for offset in range(0, num_examples, BATCH_SIZE):
#             end = offset + BATCH_SIZE
#             batch_x, batch_y = X_train[offset:end], y_train[offset:end]
#             sess.run(training_operation, feed_dict={x: batch_x, y: batch_y, keep_prob: 0.5})
#
#         validation_accuracy = evaluate(X_valid, y_valid)
#         print("EPOCH {} ...".format(i + 1))
#         print("Validation Accuracy = {:.3f}".format(validation_accuracy))
#         print()
#
#     saver.save(sess, './lenet')
#     print("Model saved")

"""Restore for real case"""
X_real, y_real = convert_images()

with tf.Session() as sess:
    saver.restore(sess, tf.train.latest_checkpoint('.'))

    test_accuracy = evaluate(X_real, y_real)
    print("Test Accuracy = {:.3f}".format(test_accuracy))
