import os

import eyed3
from PIL import Image, ImageDraw, ImageFont
import re
from xpinyin import Pinyin

# Global Variables
p = Pinyin()
input_path = 'Factory'
image_cache_path = 'ImageCache'
pixel = 600
middle = pixel / 2
margin = 0
max_char_len_per_line = 5
font_size = 120

#   for linux
# unicode_font = ImageFont.truetype("wqy-microhei.ttc", font_size)
#   目前不支持韩文
unicode_font = ImageFont.truetype("msyhbd.ttc", font_size)


def main():
    # text = u"你好 - ひらがな - 히라가나"
    # text = u"你好 - 一二你好你好"
    # img = drawBlackEmpty()
    # writeTextOnImage(img, text, "你好", "一二你好你好")
    # saveImage(img, "hello")

    file_names = readFileNames(input_path)
    for name in file_names:
        img = drawBlackEmpty()
        print(name)
        pure_name = name[:-4]
        print(pure_name)
        artist = str(pure_name.split(' - ')[:1])[2:-2]
        song_title = str(pure_name.split(' - ')[1:])[2:-2]
        if (is_ascii(song_title) == False) and (is_ascii(artist) == False):
            if is_ascii(song_title) == False:
                writeTextOnImage(img, pure_name, artist, song_title)
                saveImage(img, pure_name)

            pinyin_artist = p.get_pinyin(artist)
            pinyin_artist = re.sub("-", " ", pinyin_artist).title()
            pinyin_artist = remove_duplicate_space(pinyin_artist)
            pinyin_title = p.get_pinyin(song_title)
            pinyin_title = re.sub("-", " ", pinyin_title).title()
            pinyin_title = remove_duplicate_space(pinyin_title)

            audiofile = eyed3.load(input_path + '\\' + name)
            # print(pinyin_artist)
            # print(pinyin_title)
            try:
                audiofile.tag.artist = pinyin_artist
                if is_ascii(song_title) == False:
                    audiofile.tag.title = pinyin_title
                    imagedata = open(image_cache_path + '\\' + pure_name + ".jpg", "rb").read()
                    audiofile.tag.images.set(3, imagedata, "image/jpeg", u"you can put a description here")
                audiofile.tag.save()
            except AttributeError:
                print(pure_name + ' caused attribute error')


def is_ascii(s):
    return all(ord(c) < 128 for c in s)


def remove_duplicate_space(string):
    string = ' '.join(string.split())
    return string


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color='black')


def findNumberOfLines(text):
    song_title_len = len(str(text.split(' - ')[1:])[2:-2])
    artist_len = len(str(text.split(' - ')[:1])[2:-2])

    if (song_title_len + artist_len) < max_char_len_per_line:
        return 1
    else:
        number_of_lines = song_title_len / max_char_len_per_line
        if song_title_len % max_char_len_per_line:
            number_of_lines = number_of_lines + 1
        return int(number_of_lines + 1)


def findLineSpace(num_of_lines):
    return 25 + 10 * (4 - num_of_lines)


def writeTextOnImage(img, text, artist, song_title):
    num_of_lines = findNumberOfLines(text)
    line_space = findLineSpace(num_of_lines)

    if num_of_lines == 1:
        start_position = middle - font_size / 2
        ImageDraw.Draw(img).text((margin, start_position), text, font=unicode_font)
    elif num_of_lines == 2:
        start_position = middle - font_size - line_space / 2
        ImageDraw.Draw(img).text((margin, start_position), artist, font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space), song_title, font=unicode_font)
    elif num_of_lines == 3:
        start_position = middle - font_size / 2 - line_space - font_size
        ImageDraw.Draw(img).text((margin, start_position), artist, font=unicode_font)
        if len(song_title) == max_char_len_per_line + 1:  # one char out
            song_title_1 = song_title[:-2]
            song_title_2 = song_title[-2:]
        else:
            song_title_1 = song_title[:max_char_len_per_line]
            song_title_2 = song_title[max_char_len_per_line:]
        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space), song_title_1, font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space + font_size + line_space),
                                 song_title_2, font=unicode_font)

    else:  # 4 lines is max
        start_position = middle - font_size - line_space / 2 - line_space - font_size
        ImageDraw.Draw(img).text((margin, start_position), artist, font=unicode_font)

        song_title_1 = song_title[:max_char_len_per_line]
        song_title_2 = song_title[max_char_len_per_line:(2 * max_char_len_per_line)]
        song_title_3 = song_title[(2 * max_char_len_per_line):]

        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space), song_title_1, font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + 2 * font_size + 2 * line_space), song_title_2,
                                 font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + 3 * font_size + 3 * line_space), song_title_3,
                                 font=unicode_font)


def saveImage(img, filename):
    img.save(image_cache_path + '\\' + filename + '.jpg')


def readFileNames(path):
    return os.listdir(path)


if __name__ == '__main__':
    main()
