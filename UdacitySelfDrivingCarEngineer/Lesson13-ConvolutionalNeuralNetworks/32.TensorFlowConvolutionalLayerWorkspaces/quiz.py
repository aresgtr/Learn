import tensorflow as tf
import numpy as np

"""
Setup the strides, padding and filter weight/bias such that
the output shape is (1, 2, 2, 3).
"""
# `tf.nn.conv2d` requires the input be 4D (batch_size, height, width, depth)
# (1, 4, 4, 1)
x = np.array([
    [0, 1, 0.5, 10],
    [2, 2.5, 1, -8],
    [4, 0, 5, 6],
    [15, 1, 2, 3]], dtype=np.float32).reshape((1, 4, 4, 1))
X = tf.constant(x)


def conv2d(input_array):
    # Filter (weights and bias)
    # The shape of the filter weight is (height, width, input_depth, output_depth)
    # The shape of the filter bias is (output_depth,)
    # TODO: Define the filter weights `F_W` and filter bias `F_b`.
    # NOTE: Remember to wrap them in `tf.Variable`, they are trainable parameters after all.
    F_W = tf.Variable(tf.truncated_normal((2, 2, 1, 3)))  # out_height, out_width, in_depth, out_depth
    F_b = tf.Variable(tf.zeros(3))
    # TODO: Set the stride for each dimension (batch_size, height, width, depth)
    strides = [1, 2, 2, 1]
    """上面的怎么算出来的 (1,4,4,1) to (1,2,2,3)"""
    """strides两边都是1，里面最好也是1"""
    in_height = 4
    in_width = 4
    filter_height = 2
    filter_width = 2
    out_height = np.ceil(float(in_height - filter_height + 1) / float(strides[1]))
    out_width = np.ceil(float(in_width - filter_width + 1) / float(strides[2]))
    print(out_height)
    print(out_width)
    """用上面这个反推filter width height"""
    # TODO: set the padding, either 'VALID' or 'SAME'.
    padding = 'VALID'
    # https://www.tensorflow.org/versions/r0.11/api_docs/python/nn.html#conv2d
    # `tf.nn.conv2d` does not include the bias computation so we have to add it ourselves after.
    return tf.nn.conv2d(input_array, F_W, strides, padding) + F_b


output = conv2d(X)
print(output)
