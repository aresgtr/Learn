import hashlib
import os
import pickle
from urllib.request import urlretrieve

import numpy as np
from PIL import Image
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelBinarizer
from sklearn.utils import resample
from tqdm import tqdm
from zipfile import ZipFile

print('All modules imported.')


def main():
    # Download the training and test dataset.
    download('https://s3.amazonaws.com/udacity-sdc/notMNIST_train.zip', 'notMNIST_train.zip')
    download('https://s3.amazonaws.com/udacity-sdc/notMNIST_test.zip', 'notMNIST_test.zip')

    # Make sure the files aren't corrupted
    assert hashlib.md5(open('notMNIST_train.zip', 'rb').read()).hexdigest() == 'c8673b3f28f489e9cdf3a3d74e2ac8fa', \
        'notMNIST_train.zip file is corrupted.  Remove the file and try again.'
    assert hashlib.md5(open('notMNIST_test.zip', 'rb').read()).hexdigest() == '5d3c7e653e63471c88df796156a9dfa9', \
        'notMNIST_test.zip file is corrupted.  Remove the file and try again.'

    # Wait until you see that all files have been downloaded.
    print('All files downloaded.')

    """"""

    # Get the features and labels from the zip files
    train_features, train_labels = uncompress_features_labels('notMNIST_train.zip')
    test_features, test_labels = uncompress_features_labels('notMNIST_test.zip')

    # Limit the amount of data to work with a docker container
    docker_size_limit = 150000
    train_features, train_labels = resample(train_features, train_labels, n_samples=docker_size_limit)

    # Set flags for feature engineering.  This will prevent you from skipping an important step.
    is_features_normal = False
    is_labels_encod = False

    # Wait until you see that all features and labels have been uncompressed.
    print('All features and labels uncompressed.')

    """"""

    # Problem 1 - Implement Min-Max scaling for grayscale image data
    ### DON'T MODIFY ANYTHING BELOW ###
    # Test Cases
    np.testing.assert_array_almost_equal(
        normalize_grayscale(np.array([0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 255])),
        [0.1, 0.103137254902, 0.106274509804, 0.109411764706, 0.112549019608, 0.11568627451, 0.118823529412, 0.121960784314,
         0.125098039216, 0.128235294118, 0.13137254902, 0.9],
        decimal=3)
    np.testing.assert_array_almost_equal(
        normalize_grayscale(np.array([0, 1, 10, 20, 30, 40, 233, 244, 254,255])),
        [0.1, 0.103137254902, 0.13137254902, 0.162745098039, 0.194117647059, 0.225490196078, 0.830980392157, 0.865490196078,
         0.896862745098, 0.9])

    if not is_features_normal:
        train_features = normalize_grayscale(train_features)
        test_features = normalize_grayscale(test_features)
        is_features_normal = True

    print('Tests Passed!')


def download(url, file):
    """
    Download file from <url>
    :param url: URL to file
    :param file: Local file path
    """
    if not os.path.isfile(file):
        print('Downloading ' + file + '...')
        urlretrieve(url, file)
        print('Download Finished')


def uncompress_features_labels(file):
    """
    Uncompress features and labels from a zip file
    :param file: The zip file to extract the data from
    """
    features = []
    labels = []

    with ZipFile(file) as zipf:
        # Progress Bar
        filenames_pbar = tqdm(zipf.namelist(), unit='files')

        # Get features and labels from all files
        for filename in filenames_pbar:
            # Check if the file is a directory
            if not filename.endswith('/'):
                with zipf.open(filename) as image_file:
                    image = Image.open(image_file)
                    image.load()
                    # Load image data as 1 dimensional array
                    # We're using float32 to save on memory space
                    feature = np.array(image, dtype=np.float32).flatten()

                # Get the the letter from the filename.  This is the letter of the image.
                label = os.path.split(filename)[1][0]

                features.append(feature)
                labels.append(label)
    return np.array(features), np.array(labels)


# Problem 1 - Implement Min-Max scaling for grayscale image data
def normalize_grayscale(image_data):
    """
    Normalize the image data with Min-Max scaling to a range of [0.1, 0.9]
    :param image_data: The image data to be normalized
    :return: Normalized image data
    """
    # TODO: Implement Min-Max scaling for grayscale image data


if __name__ == '__main__':
    main()
