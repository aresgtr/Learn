import tensorflow as tf

# x = tf.add(5, 2)
#
# x = tf.substract(10, 4)
# y = tf.multiply(2, 5)

x = tf.subtract(tf.cast(tf.constant(2.0), tf.int32), tf.constant(1))

with tf.Session() as sess:
    output = sess.run(x)
    print(output)

"""
Quiz below
"""

# TODO: Convert the following to TensorFlow:
x = tf.constant(10)
y = tf.constant(2)
z = tf.subtract(tf.divide(x, y), 1)

# TODO: Print z from a session as the variable output
output = None

with tf.Session() as sess:
    output = sess.run(z)
    print(output)
