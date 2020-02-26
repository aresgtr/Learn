from PIL import Image, ImageDraw, ImageFont

# Global Variables
input_path = 'Factory'
image_cache_path = 'ImageCache'
pixel = 600
middle = pixel / 2
margin = 60
max_char_len_per_line = 7
font_size = 80
line_space = 40


def main():
    img = drawBlackEmpty()


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color='black')

    name = 'hihi'
    song_name = 'hihihi'

    if is_ascii(song_title) == False:
        writeTextOnImage(img, pure_name, artist, song_title)
        saveImage(img, pure_name)


def is_ascii(s):
    return all(ord(c) < 128 for c in s)


def writeTextOnImage(img, text, artist, song_title):
    num_of_lines = findNumberOfLines(text)

    #   for linux
    # unicode_font = ImageFont.truetype("wqy-microhei.ttc", font_size)
    #   目前不支持韩文
    unicode_font = ImageFont.truetype("STKAITI.TTF", font_size)

    if num_of_lines == 1:
        start_position = middle - font_size / 2
        ImageDraw.Draw(img).text((margin, start_position), text, font=unicode_font)
    elif num_of_lines == 2:
        start_position = middle - font_size - line_space / 2
        ImageDraw.Draw(img).text((margin, start_position), artist + ' -', font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space), song_title, font=unicode_font)
    elif num_of_lines == 3:
        start_position = middle - font_size / 2 - line_space - font_size
        ImageDraw.Draw(img).text((margin, start_position), artist + ' -', font=unicode_font)
        if len(song_title) == max_char_len_per_line:  # one char out
            song_title_1 = song_title[:-2]
            song_title_2 = song_title[-2:]
        else:
            song_title_1 = song_title[:max_char_len_per_line - 1]
            song_title_2 = song_title[max_char_len_per_line - 1:]
        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space), song_title_1, font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space + font_size + line_space),
                                 song_title_2, font=unicode_font)

    elif num_of_lines == 4:
        start_position = middle - font_size - line_space / 2 - line_space - font_size
        ImageDraw.Draw(img).text((margin, start_position), artist + ' -', font=unicode_font)

        song_title_1 = song_title[:max_char_len_per_line - 1]
        song_title_2 = song_title[max_char_len_per_line - 1:(2 * max_char_len_per_line - 2)]
        song_title_3 = song_title[(2 * max_char_len_per_line - 2):]

        ImageDraw.Draw(img).text((margin, start_position + font_size + line_space), song_title_1, font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + 2 * font_size + 2 * line_space), song_title_2,
                                 font=unicode_font)
        ImageDraw.Draw(img).text((margin, start_position + 3 * font_size + 3 * line_space), song_title_2,
                                 font=unicode_font)


def findNumberOfLines(text):
    song_title_len = len(str(text.split(' - ')[1:])[2:-2])

    if (len(text) - 1) <= max_char_len_per_line:
        return 1
    else:
        number_of_lines = song_title_len / (max_char_len_per_line - 1)
        if song_title_len % (max_char_len_per_line - 1):
            number_of_lines = number_of_lines + 1
        return int(number_of_lines + 1)


if __name__ == '__main__':
    main()
