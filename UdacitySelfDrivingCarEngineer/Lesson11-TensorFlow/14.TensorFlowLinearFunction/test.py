import tensorflow as tf

x = tf.Variable(5)

init = tf.global_variables_initializer()

with tf.Session() as sess:
    sess.run(init)
    out = sess.run(x)
    print(out)

n_features = 120
n_labels = 5
weights = tf.Variable(tf.truncated_normal((n_features, n_labels)))

n_labels = 5
bias = tf.Variable(tf.zeros(n_labels))