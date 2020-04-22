# Solution is available in the other "solution.py" tab
import numpy as np


'''
This is numpy softmax function. TensorFlow have built-in softmax.
'''

def softmax(x):
    """Compute softmax values for each sets of scores in x."""
    # TODO: Compute and return softmax(x)
    return np.exp(x) / np.sum(np.exp(x), axis=0)

logits = [3.0, 1.0, 0.2]
print(softmax(logits))
