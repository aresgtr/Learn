from PIL import Image, ImageDraw, ImageFont
import os

inputPath = 'FactoryIn'


def main():
    img = drawBlackEmpty()
    text = u"编程方法论 - ひらがな - Hiragana, 히라가나"
    writeTextOnImage(img, text)
    saveImage(img, 'hello')

    fileNames = readFileNames(inputPath)
    print(fileNames)


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color='black')


def writeTextOnImage(img, text):
    font_size = 35
    unicode_font = ImageFont.truetype("DejaVuSans.ttf", font_size)
    ImageDraw.Draw(img).text((100, 100), text, font=unicode_font)


def saveImage(img, filename):
    img.save(filename + '.jpg')


def readFileNames(path):
    return os.listdir(path)


if __name__ == '__main__':
    main()
