import os

import eyed3
from PIL import Image, ImageDraw, ImageFont


def main():
    img = drawBlackEmpty()
    text = u"你好 - ひらがな - 히라가나"
    writeTextOnImage(img, text)
    saveImage(img, 'hello')
    inputPath = 'FactoryIn'
    imageCachePath = 'ImageCache'

    fileNames = readFileNames(inputPath)
    for name in fileNames:
        audiofile = eyed3.load(inputPath + '\\' + name)
        imagedata = open("hello.jpg", "rb").read()
        audiofile.tag.images.set(3, imagedata, "image/jpeg", u"you can put a description here")
        audiofile.tag.save()


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color='black')


def writeTextOnImage(img, text):
    font_size = 35
    #   for linux
    # unicode_font = ImageFont.truetype("wqy-microhei.ttc", font_size)
    #   目前不支持韩文
    unicode_font = ImageFont.truetype("STKAITI.TTF", font_size)
    ImageDraw.Draw(img).text((100, 100), text, font=unicode_font)


def saveImage(img, filename):
    img.save(filename + '.jpg')


def readFileNames(path):
    return os.listdir(path)


if __name__ == '__main__':
    main()
