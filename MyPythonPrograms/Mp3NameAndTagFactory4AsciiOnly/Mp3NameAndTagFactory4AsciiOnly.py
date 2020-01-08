import os

import eyed3
from PIL import Image, ImageDraw, ImageFont

# Global Variables
input_path = 'FactoryIn'
image_cache_path = 'ImageCache'
middle = 250
max_char_len_per_line = 12


def main():

    img = drawBlackEmpty()

    # text = u"你好 - ひらがな - 히라가나"
    text = u"你好 - 你好你好你好你好你好你好你好"

    num_of_lines = findNumberOfLines(text)
    print(num_of_lines)
    print(text.split(' - ')[1:])

    if num_of_lines > 1:
        artist = text.split(' - ')[:1]
        song_name = text.split(' - ')[1:]


    writeTextOnImage(img, text, middle)
    saveImage(img, 'hello')

    file_names = readFileNames(input_path)
    for name in file_names:
        audiofile = eyed3.load(input_path + '\\' + name)
        imagedata = open("hello.jpg", "rb").read()
        audiofile.tag.images.set(3, imagedata, "image/jpeg", u"you can put a description here")
        audiofile.tag.save()


def findNumberOfLines(text):
    num_of_lines = int(len(text) / max_char_len_per_line)
    if len(text) % max_char_len_per_line:
        num_of_lines = num_of_lines + 1
    return num_of_lines


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color='black')


def writeTextOnImage(img, text, start_position):
    font_size = 35
    #   for linux
    unicode_font = ImageFont.truetype("wqy-microhei.ttc", font_size)
    #   目前不支持韩文
    # unicode_font = ImageFont.truetype("STKAITI.TTF", font_size)
    ImageDraw.Draw(img).text((100, start_position), text, font=unicode_font)


def saveImage(img, filename):
    img.save(filename + '.jpg')


def readFileNames(path):
    return os.listdir(path)


if __name__ == '__main__':
    main()
