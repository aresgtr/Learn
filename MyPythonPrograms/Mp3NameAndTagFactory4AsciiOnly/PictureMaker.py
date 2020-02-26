from PIL import Image, ImageDraw, ImageFont

# Global Variables
pixel = 600
middle = pixel / 2
margin = 0
max_char_len_per_line = 5
font_size = 120

unicode_font = ImageFont.truetype("msyhbd.ttc", font_size)


def main():

    # TODO: for testing, will be removed
    # artist = '目前'
    # song_name = '目前不支持韩文目前不支持韩文'
    # text = '目前 - 目前不支持韩文目前不支持韩文'

    file = open('input.txt', 'r', encoding='utf-8')
    Lines = file.readlines()

    for text in Lines:
        artist = text.split(' - ')[0].rstrip()
        song_name = text.split(' - ')[1].rstrip()

        if not is_ascii(song_name):
            img = drawBlackEmpty()
            writeTextOnImage(img, text, artist, song_name)
            saveImage(img, song_name)


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color='black')


def is_ascii(s):
    return all(ord(c) < 128 for c in s)


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


def saveImage(img, filename):
    img.save(filename + '.jpg')


if __name__ == '__main__':
    main()
