from PIL import Image, ImageDraw
import os

inputPath = 'FactoryIn'

def main():
    img = drawBlackEmpty()
    writeTextOnImage(img, "Hello")
    saveImage(img, 'hello')

    fileNames = readFileNames(inputPath)
    print(fileNames)

def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color = 'black')

def writeTextOnImage(img, text):
    ImageDraw.Draw(img).text((100, 100), text, fill = (255, 255, 0))

def saveImage(img, filename):
    img.save(filename + '.jpg')

def readFileNames(path):
    return os.listdir(path)

if __name__ == '__main__':
    main()