"""
This quiz will be starting with the code from the ReLU Quiz and applying a dropout layer.
Build a model with a ReLU layer and dropout layer using the keep_prob placeholder to pass in a probability of 0.5.
Print the logits from the model.
"""

import tensorflow as tf
from test import *
tf.set_random_seed(123456)

output = None
hidden_layer_weights = [
    [0.1, 0.2, 0.4],
    [0.4, 0.6, 0.6],
    [0.5, 0.9, 0.1],
    [0.8, 0.2, 0.8]]
out_weights = [
    [0.1, 0.6],
    [0.2, 0.1],
    [0.7, 0.9]]

# Weights and biases (for both hidden layer and output layer)
weights = [
    tf.Variable(hidden_layer_weights),
    tf.Variable(out_weights)]
biases = [
    tf.Variable(tf.zeros(3)),
    tf.Variable(tf.zeros(2))]

# Input
features = tf.Variable([[1.0, 2.0, 3.0, 4.0], [-1.0, -2.0, -3.0, -4.0], [11.0, 12.0, 13.0, 14.0]])

# TODO: Create Model
keep_prob = tf.placeholder(tf.float32)
# Hidden Layer with ReLU activation function
hidden_layer = tf.add(tf.matmul(features, weights[0]), biases[0])
hidden_layer = tf.nn.relu(hidden_layer)
hidden_layer = tf.nn.dropout(hidden_layer, keep_prob)   # 加个dropout
logits = tf.add(tf.matmul(hidden_layer, weights[1]), biases[1])

# TODO: save and print session results on a variable named "output"
with tf.Session() as sess:
    sess.run(tf.global_variables_initializer())
    output = sess.run(logits, feed_dict={keep_prob: 0.5})
    print(output)
