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

    if num_of_lines > 1:
        artist = text.split(' - ')[:1]
        song_name = text.split(' - ')[1:]


    writeTextOnImage(img, text, 2)
    saveImage(img, 'hello')

    file_names = readFileNames(input_path)
    for name in file_names:

        num_of_lines = findNumberOfLines(name)


        writeTextOnImage(img, text, num_of_lines)

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


def writeTextOnImage(img, text, num_of_lines):
    font_size = 50
    #   for linux
    unicode_font = ImageFont.truetype("wqy-microhei.ttc", font_size)
    #   目前不支持韩文
    # unicode_font = ImageFont.truetype("STKAITI.TTF", font_size)




    if num_of_lines == 1:
        start_position = middle
        ImageDraw.Draw(img).text((100, start_position), text, font=unicode_font)
    elif num_of_lines == 2:
        artist = str(text.split(' - ')[:1])[2:-2] + ' -'
        song_title = str(text.split(' - ')[1:])[2:-2]
        print(artist)
        print(song_title)
        start_position = middle - 50
        ImageDraw.Draw(img).text((100, start_position), artist, font=unicode_font)
        ImageDraw.Draw(img).text((100, start_position + 100), song_title, font=unicode_font)




def saveImage(img, filename):
    img.save(filename + '.jpg')


def readFileNames(path):
    return os.listdir(path)


if __name__ == '__main__':
    main()
