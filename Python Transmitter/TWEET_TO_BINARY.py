import RPi.GPIO as GPIO

import time
filename = open("statuses.txt",'r')
read_tweet_list = filename.read().split("\n")

    
def set_led_array(led_list):
    # all the GPIO pins are setup to switch on their pins at a given command
    GPIO.setmode(GPIO.BOARD)
    GPIO.setup(led_list, GPIO.OUT)

def laseoff(led_list):
    # the entire array and their corresponding GPIO pins are turned off
    GPIO.output(led_list,False)
def laseon(led_list):
    # the entire array and their corresponding GPIO pins are turned off
    GPIO.output(led_list,True)


def LED_seizure(led_list,duration,blink_delay):
    # very simple function turns all the led's and their respective GPIO pins on/off at certain intervals with
    # predetermined delays.
    for i in range(duration):
        time.sleep(blink_delay)
        turn_array_off(led_list)
        time.sleep(blink_delay)
        turn_array_on(led_list)
def binary(input_str):
    for i in input_str:
        print(i+" ")

def toBinary(num):
    result = ""
    result += bin(num)
    result = result[2:]
    padding = "0" * (8 - len(result))
    return padding + result

def asciiToBinary(string,laser,blink_delay): 
    new_str = "1"
    for i in range(0,len(string)):
        new_str += toBinary(ord(string[i]))
    print(new_str)
    for i in new_str:
        if i is '0':
            laseoff(laser)
            time.sleep(blink_delay)

        elif i is '1':
            laseon(laser)
            time.sleep(blink_delay)
    

def string_char(input_str,laser,blink_dly):
    asciiToBinary(input_str,laser,blink_dly)
       
    laseoff(laser)
    
def send_tweets(tweetlist):
    for i in tweetlist:
        string_char(i,40,0.1)
        time.sleep(2)
        
def repeat(duration,delay):
    for i in range(0,duration):
        laseon(40)
        time.sleep(delay)
        laseoff(40)
        time.sleep(delay)
        
    
led_tuple = (40)
set_led_array(led_tuple)
laseoff(led_tuple)
send_tweets(read_tweet_list )





