import glob
import ntpath
import cv2
import matplotlib.pyplot as plt
import tensorflow as tf


def main():
    saver = tf.train.Saver()

    X_real, y_real = convert_images()

    with tf.Session() as sess:
        saver.restore(sess, tf.train.latest_checkpoint('.'))

        test_accuracy = evaluate(X_real, y_real)
        print("Test Accuracy = {:.3f}".format(test_accuracy))



def convert_images():
    images = glob.glob('trafficsigns/*')

    dsize = (32, 32)
    X_real = []
    y_real = []

    for idx, fname in enumerate(images):
        image = cv2.imread(fname)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        resized = cv2.resize(image, dsize)
        X_real.append(resized)
        y_real.append(ntpath.basename(fname).split('.')[0])

    return X_real, y_real



if __name__ == '__main__':
    main()
