import os

import eyed3
from PIL import Image, ImageDraw, ImageFont
import re
from xpinyin import Pinyin

# Global Variables
p = Pinyin()
input_path = 'FactoryIn'
image_cache_path = 'ImageCache'
middle = 225
margin = 80
max_char_len_per_line = 8
font_size = 80

def main():

    img = drawBlackEmpty()

    # text = u"你好 - ひらがな - 히라가나"
    text = u"你好 - 一二三四五六七八"

    #num_of_lines = findNumberOfLines(text)

    #writeTextOnImage(img, text, 2)
    #saveImage(img, 'hello')

    file_names = readFileNames(input_path)
    for name in file_names:
        pure_name = str(name.split('.')[:1])[2:-2]
        artist = str(pure_name.split(' - ')[:1])[2:-2] + ' -'
        song_title = str(pure_name.split(' - ')[1:])[2:-2]

        writeTextOnImage(img, pure_name)
        saveImage(img, pure_name)

        pinyin_artist = p.get_pinyin(artist)
        pinyin_artist = re.sub("-", " ", pinyin_artist).title()
        pinyin_artist = remove_duplicate_space(pinyin_artist)
        pinyin_title = p.get_pinyin(song_title)
        pinyin_title = re.sub("-", " ", pinyin_title).title()
        pinyin_title = remove_duplicate_space(pinyin_title)

        audiofile = eyed3.load(input_path + '\\' + name)
        audiofile.tag.artist = pinyin_artist
        # print(audiofile.tag.album)
        audiofile.tag.title = pinyin_title
        # print(audiofile.tag.track_num)

        imagedata = open(pure_name + ".jpg", "rb").read()
        audiofile.tag.images.set(3, imagedata, "image/jpeg", u"you can put a description here")
        audiofile.tag.save()

def remove_duplicate_space(string):
    string = ' '.join(string.split())

    return string

def findNumberOfLines(text):
    num_of_lines = int(len(text) / max_char_len_per_line)
    if len(text) % max_char_len_per_line:
        num_of_lines = num_of_lines + 1
    return num_of_lines


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color='black')


def writeTextOnImage(img, text):
    num_of_lines = findNumberOfLines(text)

    #   for linux
    # unicode_font = ImageFont.truetype("wqy-microhei.ttc", font_size)
    #   目前不支持韩文
    unicode_font = ImageFont.truetype("STKAITI.TTF", font_size)

    if num_of_lines == 1:
        start_position = middle
        ImageDraw.Draw(img).text((margin, start_position), text, font=unicode_font)
    elif num_of_lines == 2:
        artist = str(text.split(' - ')[:1])[2:-2] + ' -'
        song_title = str(text.split(' - ')[1:])[2:-2]
        start_position = middle - 50
        ImageDraw.Draw(img).text((margin, start_position), artist, font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + 100), song_title, font=unicode_font)

def saveImage(img, filename):
    img.save(filename + '.jpg')


def readFileNames(path):
    return os.listdir(path)


if __name__ == '__main__':
    main()
