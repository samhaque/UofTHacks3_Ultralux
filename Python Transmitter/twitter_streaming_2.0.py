# Import the necessary package to process data in JSON format
try:
    import json
except ImportError:
    import simplejson as json

# Import the necessary methods from "twitter" library
from twitter import Twitter, OAuth, TwitterHTTPError, TwitterStream

export_file = open('statuses.txt','w')

# Variables that contains the user credentials to access Twitter API 
ACCESS_TOKEN = '797449970-jIbhS1yhbxCHMDzTmoeOLlcKYErE8ibzGxTEEK3x'
ACCESS_SECRET = 'Cq7RUGLVZt4HJRkZL5JDkMxU3x0sMEcH9V0GgplxMLBiq'
CONSUMER_KEY = 'Bk9zXQlsm5JRatNOhJ4ZCDwP5'
CONSUMER_SECRET = 'HWsqgNTklEo2xto661869qzs2Ul8c9NfW0WOLzCrjgaQjbNOu3'

oauth = OAuth(ACCESS_TOKEN, ACCESS_SECRET, CONSUMER_KEY, CONSUMER_SECRET)

# Initiate the connection to Twitter Streaming API
twitter_stream = TwitterStream(auth=oauth)

# Get a sample of the public data following through Twitter
iterator = twitter_stream.statuses.filter(track="storm", language="en")

# Print each tweet in the stream to the screen 
# Here we set it to stop after getting 1000 tweets. 
# You don't have to set it to stop, but can continue running 
# the Twitter API to collect data for days or even longer. 
tweet_count = 50
for tweet in iterator:
    tweet_count -= 1
    # Twitter Python Tool wraps the data returned by Twitter 
    # as a TwitterDictResponse object.
    # We convert it back to the JSON format to print/score
    try:
        #print "\n"
        #print tweet['text']
        #print tweet['user']['screen_name']

        if 'text' in tweet:
            export_file.write(tweet['text'])
            export_file.write("\n")
    except:
        continue
    # The command below will do pretty printing for JSON data, try it out
    # print json.dumps(tweet, indent=4)
       
    if tweet_count <= 0:
        break

export_file.close()