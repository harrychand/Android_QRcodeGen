#!/usr/bin/env python
# Author: Wilson Choo, Harry Chand, John Ho

# Copyright (c) 2018 Intel Corporation.
#
# Permission is hereby granted, free of charge, to any person obtaining
# a copy of this software and associated documentation files (the
# "Software"), to deal in the Software without restriction, including
# without limitation the rights to use, copy, modify, merge, publish,
# distribute, sublicense, and/or sell copies of the Software, and to
# permit persons to whom the Software is furnished to do so, subject to
# the following conditions:
#
# The above copyright notice and this permission notice shall be
# included in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
# EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
# NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
# LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
# OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
# WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

from upm import pyupm_jhd1313m1 as lcd
import mraa
import time
import threading
import zbar

from PIL import Image
import cv2

#Defination and Initilization 
mraa.addSubplatform(mraa.GROVEPI, "0")

print (mraa.getVersion())
#LED hooked up to digital port D3, offset:512, so 3+512=515
pin = mraa.Gpio(515)   
pin.dir(mraa.DIR_OUT)
# Initialize Jhd1313m1 at 0x3E (LCD_ADDRESS) and 0x62 (RGB_ADDRESS)
myLcd = lcd.Jhd1313m1(0, 0x3E, 0x62)
zbar_image = None

def qr_scanner():
    """
    A simple function that captures webcam video utilizing OpenCV. The video is then broken down into frames which
    are constantly displayed. The frame is then converted to grayscale for better contrast. Afterwards, the image
    is transformed into a numpy array using PIL. This is needed to create zbar image. This zbar image is then scanned
    utilizing zbar's image scanner and will then print the decodeed message of any QR or bar code. To quit the program,
    press "q".
    :return:
    """
    print "Start WebCam"
    # Begin capturing video. You can modify what video source to use with VideoCapture's argument. It's currently set
    # to be your webcam.
    capture = cv2.VideoCapture(0)
    newdata = ""
    QRdata = ""
    mypayment = ""

    while True:
        # To quit this program press q.
        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

        # Breaks down the video into frames
        ret, frame = capture.read()

        # Displays the current frame
        cv2.imshow('Current', frame)

        # Converts image to grayscale.
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

        # Uses PIL to convert the grayscale image into a ndary array that ZBar can understand.
        image = Image.fromarray(gray)
        width, height = image.size
	global zbar_image
	global QRdata
        zbar_image = zbar.Image(width, height, 'Y800', image.tostring())

	#topLeftCorners, bottomLeftCorners, bottomRightCorners, topRightCorners = [item for item in symbol.location]
	#for (x,y,w,h) in zbar_image:
        #        cv2.rectangle(frame,(x,y),(x+w,y+h),(255,0,0),2)
        #        roi_gray = gray[y:y+h,x:x+w]
        #        roi_color = frame[y:y+h,x:x+w]

        # Scans the zbar image.
        scanner = zbar.ImageScanner()
        scanner.scan(zbar_image)
	
        # Prints data from image.
        for decoded in zbar_image:
		QRdata = decoded.data
            	#print "QR code detected!"
		#print(decoded.data)
	mypayment = QRdata.split(',')
		#print mypayment[0]
	if mypayment[0] == "PaymentDone" and QRdata != newdata :
		PaymentDone(QRdata)
		newdata = QRdata
	elif QRdata != newdata: 
		#download_thread = threading.Thread(target=BarrierActive, args=(QRdata))
		#download_thread.daemon = True
    		#download_thread.start()
		BarrierActive(QRdata)	
		newdata = QRdata	

def BarrierActive(mydata):
	myLcd.clear()
	mylist = mydata.split(',')
	print mylist[0]
	pin.write(1)
	myLcd.setColor(0, 255, 0)
        myLcd.setCursor(0,0)
        myLcd.write('Welcome '+ str(mylist[0]))
        myLcd.setCursor(1,2)
        myLcd.write('Car# '+ str(mylist[1]))
	time.sleep(5)
	myLcd.clear()
	DefaultDisplay()

def DefaultDisplay():
	pin.write(0)
        myLcd.setColor(255, 0, 0)
        myLcd.setCursor(0,0)
        myLcd.write('Welcome2 Parking')
        myLcd.setCursor(1,2)
        myLcd.write('Scan QRcode')

def PaymentDone(mymoney):
	myLcd.clear()
	myrich = mymoney.split(',')
	print myrich[0]
        pin.write(1)
        myLcd.setColor(0, 0, 255)
        myLcd.setCursor(0,0)
        myLcd.write('Tq '+str(myrich[1]))
        myLcd.setCursor(1,2)
        myLcd.write('Good Bye!')
	time.sleep(5)
	myLcd.clear()
	DefaultDisplay()
	
def main():
	pin.write(0)
	myLcd.clear()
	print "Welcome to Smart Parking "
	DefaultDisplay()
	qr_scanner()
	

if __name__ == "__main__":
	main()
