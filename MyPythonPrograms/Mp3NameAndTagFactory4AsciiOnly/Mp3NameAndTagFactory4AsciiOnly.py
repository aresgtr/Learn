from PIL import Image, ImageDraw

def main():
    img = drawBlackEmpty()
    writeTextOnImage(img, "Hello")
    saveImage(img, 'hello')


def drawBlackEmpty():
    return Image.new('RGB', (600, 600), color = 'black')

def writeTextOnImage(img, text):
    ImageDraw.Draw(img).text((100, 100), text, fill = (255, 255, 0))


def saveImage(img, filename):
    img.save(filename + '.jpg')

if __name__ == '__main__':
    main()