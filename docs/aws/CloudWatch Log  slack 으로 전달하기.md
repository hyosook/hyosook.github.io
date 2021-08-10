# CloudWatch Log  slack 으로 전달하기 



## 구성

ELB Log  -> cloudwatch  -> filter 생성 - > lambda 트리거 - > slcak 



## slcak

https://app.slack.com/block-kit-builder/T01TH6G3D17#%7B%22attachments%22:%5B%7B%22color%22:%22#f2c744%22,%22blocks%22:%5B%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22Hello,%20Assistant%20to%20the%20Regional%20Manager%20Dwight!%20*Michael%20Scott*%20wants%20to%20know%20where%20you'd%20like%20to%20take%20the%20Paper%20Company%20investors%20to%20dinner%20tonight.%5Cn%5Cn%20*Please%20select%20a%20restaurant:*%22%7D%7D,%7B%22type%22:%22divider%22%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Farmhouse%20Thai%20Cuisine*%5Cn:star::star::star::star:%201528%20reviews%5Cn%20They%20do%20have%20some%20vegan%20options,%20like%20the%20roti%20and%20curry,%20plus%20they%20have%20a%20ton%20of%20salad%20stuff%20and%20noodles%20can%20be%20ordered%20without%20meat!!%20They%20have%20something%20for%20everyone%20here%22%7D,%22accessory%22:%7B%22type%22:%22image%22,%22image_url%22:%22https://s3-media3.fl.yelpcdn.com/bphoto/c7ed05m9lC2EmA3Aruue7A/o.jpg%22,%22alt_text%22:%22alt%20text%20for%20image%22%7D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Kin%20Khao*%5Cn:star::star::star::star:%201638%20reviews%5Cn%20The%20sticky%20rice%20also%20goes%20wonderfully%20with%20the%20caramelized%20pork%20belly,%20which%20is%20absolutely%20melt-in-your-mouth%20and%20so%20soft.%22%7D,%22accessory%22:%7B%22type%22:%22image%22,%22image_url%22:%22https://s3-media2.fl.yelpcdn.com/bphoto/korel-1YjNtFtJlMTaC26A/o.jpg%22,%22alt_text%22:%22alt%20text%20for%20image%22%7D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22*Ler%20Ros*%5Cn:star::star::star::star:%202082%20reviews%5Cn%20I%20would%20really%20recommend%20the%20%20Yum%20Koh%20Moo%20Yang%20-%20Spicy%20lime%20dressing%20and%20roasted%20quick%20marinated%20pork%20shoulder,%20basil%20leaves,%20chili%20&%20rice%20powder.%22%7D,%22accessory%22:%7B%22type%22:%22image%22,%22image_url%22:%22https://s3-media2.fl.yelpcdn.com/bphoto/DawwNigKJ2ckPeDeDM7jAg/o.jpg%22,%22alt_text%22:%22alt%20text%20for%20image%22%7D%7D,%7B%22type%22:%22divider%22%7D,%7B%22type%22:%22actions%22,%22elements%22:%5B%7B%22type%22:%22button%22,%22text%22:%7B%22type%22:%22plain_text%22,%22text%22:%22Farmhouse%22,%22emoji%22:true%7D,%22value%22:%22click_me_123%22%7D,%7B%22type%22:%22button%22,%22text%22:%7B%22type%22:%22plain_text%22,%22text%22:%22Kin%20Khao%22,%22emoji%22:true%7D,%22value%22:%22click_me_123%22,%22url%22:%22https://google.com%22%7D,%7B%22type%22:%22button%22,%22text%22:%7B%22type%22:%22plain_text%22,%22text%22:%22Ler%20Ros%22,%22emoji%22:true%7D,%22value%22:%22click_me_123%22,%22url%22:%22https://google.com%22%7D%5D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22This%20is%20a%20mrkdwn%20section%20block%20:ghost:%20*this%20is%20bold*,%20and%20~this%20is%20crossed%20out~,%20and%20%3Chttps://google.com%7Cthis%20is%20a%20link%3E%22%7D%7D,%7B%22type%22:%22section%22,%22text%22:%7B%22type%22:%22mrkdwn%22,%22text%22:%22This%20is%20a%20mrkdwn%20section%20block%20:ghost:%20*this%20is%20bold*,%20and%20~this%20is%20crossed%20out~,%20and%20%3Chttps://google.com%7Cthis%20is%20a%20link%3E%22%7D%7D,%7B%22type%22:%22section%22,%22fields%22:%5B%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D%5D%7D,%7B%22type%22:%22section%22,%22fields%22:%5B%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D,%7B%22type%22:%22plain_text%22,%22text%22:%22*this%20is%20plain_text%20text*%22,%22emoji%22:true%7D%5D%7D%5D%7D%5D%7D

## Lambda

* 함수

  ```python
  import boto3
  import json
  import logging
  import os
  import gzip
  import urllib
  import time
  import base64
  
  
  from urllib.request import Request, urlopen
  from urllib.error import URLError, HTTPError
  
  
  ENCRYPTED_HOOK_URL = os.environ['kmsEncryptedHookUrl']
  SLACK_CHANNEL = os.environ['slackChannel']
  
  
  
  HOOK_URL = "https://" +ENCRYPTED_HOOK_URL
  
  logger = logging.getLogger()
  logger.setLevel(logging.INFO)
  
  def logpayload(event):
      logger.setLevel(logging.DEBUG)
      compressed_payload = base64.b64decode(event['awslogs']['data'])
      uncompressed_payload = gzip.decompress(compressed_payload)
      log_payload = json.loads(uncompressed_payload)
      return log_payload
  
  
  def error_details(payload):
      error_msg = []
      log_events = payload['logEvents']
      loggroup = payload['logGroup']
      logstream = payload['logStream']
      lambda_func_name = loggroup.split('/')
      for log_event in log_events:
          error_msg.append( json.loads(log_event['message'].split('web:',1)[1]))
      return loggroup, logstream, error_msg, lambda_func_name
  
  def send_slack(json_object):
      slack_message = {
      "channel": SLACK_CHANNEL,
      "attachments": [{
        "color": "#eb4034",
        "blocks": [
        {
          "type": "context",
          "elements": [
              {
                  "type": "mrkdwn",
                  "text": json_object['message']
              }
          ]
        },
          {
            "type": "section",
            "fields": [
                 {
                    "type": "mrkdwn",
                    "text": "발생 시간: " + json_object['time']
                },
                {
                    "type": "mrkdwn",
                    "text": "USER_ID: " + json_object['userId']
                }
  
            ]
          }
        ]
      }],
      "blocks": [
        {
          "type": "section",
          "text": {
              "type": "mrkdwn",
              "text":  "[ "+json_object['errCode']+" ]"
          }
        },
        {
          "type": "divider"
        }
      ]}
  
      req = Request(HOOK_URL, json.dumps(slack_message).encode('utf-8'))
      try:
          response = urlopen(req)
          response.read()
      except HTTPError as e:
          logger.error("Request failed: %d %s", e.code, e.reason)
      except URLError as e:
          logger.error("Server connection failed: %s", e.reason)
  
      
  
  def lambda_handler(event, context):
  
      pload = logpayload(event)
      lgroup, lstream, error_msg, lambdaname = error_details(pload)
      for msg in error_msg:
          send_slack(msg)
  
  
  
     
  ```

```
{
	"blocks": [
		{
			"type": "header",
			"text": {
				"type": "plain_text",
				"text": "[Gradnet 에러알림] 결제 후처리 실패 _웹훅",
				"emoji": true
			}
		},
		{
			"type": "divider"
		},
		{
			"type": "section",
			"text": {
				"type": "mrkdwn",
				"text": "*Farmhouse Thai Cuisine*\n:star::star::star::star: 1528 reviews\n They do have some vegan options, like the roti and curry, plus they have a ton of salad stuff and noodles can be ordered without meat!! They have something for everyone here"
			}
		},
		{
			"type": "divider"
		},
		{
			"type": "section",
			"fields": [
				{
					"type": "mrkdwn",
					"text": "발생 시간: "
				},
				{
					"type": "mrkdwn",
					"text": "USER_ID:"
				}
			]
		}
	]
}
```



```
{
	"attachments": [
		{
			"color": "#f2c744",
			"blocks": [
				{
					"type": "section",
					"text": {
						"type": "mrkdwn",
						"text": "Hello, Assistant to the Regional Manager Dwight! *Michael Scott* wants to know where you'd like to take the Paper Company investors to dinner tonight.\n\n *Please select a restaurant:*"
					}
				},
				{
					"type": "divider"
				},
				{
					"type": "section",
					"text": {
						"type": "mrkdwn",
						"text": "*Farmhouse Thai Cuisine*\n:star::star::star::star: 1528 reviews\n They do have some vegan options, like the roti and curry, plus they have a ton of salad stuff and noodles can be ordered without meat!! They have something for everyone here"
					},
					"accessory": {
						"type": "image",
						"image_url": "https://s3-media3.fl.yelpcdn.com/bphoto/c7ed05m9lC2EmA3Aruue7A/o.jpg",
						"alt_text": "alt text for image"
					}
				},
				{
					"type": "divider"
				}
			]
		}
	]
}
```







```pyhon
import boto3
import json
import logging
import os
import gzip
import urllib
import time
import base64


from urllib.request import Request, urlopen
from urllib.error import URLError, HTTPError


ENCRYPTED_HOOK_URL = os.environ['kmsEncryptedHookUrl']
SLACK_CHANNEL = os.environ['slackChannel']



HOOK_URL = "https://" +ENCRYPTED_HOOK_URL

logger = logging.getLogger()
logger.setLevel(logging.INFO)

def logpayload(event):
    logger.setLevel(logging.DEBUG)
    compressed_payload = base64.b64decode(event['awslogs']['data'])
    uncompressed_payload = gzip.decompress(compressed_payload)
    log_payload = json.loads(uncompressed_payload)
    return log_payload


def error_details(payload):
    error_msg = []
    log_events = payload['logEvents']
    loggroup = payload['logGroup']
    logstream = payload['logStream']
    lambda_func_name = loggroup.split('/')
    for log_event in log_events:
        error_msg.append( json.loads(log_event['message'].split('web:',1)[1]))
    return loggroup, logstream, error_msg, lambda_func_name

def send_slack(json_object):
    slack_message = {
    "channel": SLACK_CHANNEL,
    "attachments": [{
      "color": "#eb4034",
      "blocks": [
      {
        "type": "context",
        "elements": [
            {
                "type": "mrkdwn",
                "text": json_object['message']
            }
        ]
      },
        {
          "type": "section",
          "fields": [
               {
                  "type": "mrkdwn",
                  "text": "발생 시간: " + json_object['time']
              },
              {
                  "type": "mrkdwn",
                  "text": "USER_ID: " + json_object['userId']
              }

          ]
        }
      ]
    }],
    "blocks": [
      {
        "type": "header",
        "text": {
            "type": "plain_text",
            "text": "[Gradnet 에러알림] " + json_object['errCode']
        }
      },
      {
        "type": "divider"
      }
    ]}

    req = Request(HOOK_URL, json.dumps(slack_message).encode('utf-8'))
    try:
        response = urlopen(req)
        response.read()
    except HTTPError as e:
        logger.error("Request failed: %d %s", e.code, e.reason)
    except URLError as e:
        logger.error("Server connection failed: %s", e.reason)

    

def lambda_handler(event, context):

    pload = logpayload(event)
    lgroup, lstream, error_msg, lambdaname = error_details(pload)
    for msg in error_msg:
        send_slack(msg)



   
```

```python
import boto3
import json
import logging
import os
import gzip
import urllib
import time
import base64


from urllib.request import Request, urlopen
from urllib.error import URLError, HTTPError


ENCRYPTED_HOOK_URL = os.environ['kmsEncryptedHookUrl']
SLACK_CHANNEL = os.environ['slackChannel']



HOOK_URL = "https://" +ENCRYPTED_HOOK_URL

logger = logging.getLogger()
logger.setLevel(logging.INFO)

def logpayload(event):
    logger.setLevel(logging.DEBUG)
    compressed_payload = base64.b64decode(event['awslogs']['data'])
    uncompressed_payload = gzip.decompress(compressed_payload)
    log_payload = json.loads(uncompressed_payload)
    return log_payload


def error_details(payload):
    error_msg = []
    log_events = payload['logEvents']
    loggroup = payload['logGroup']
    logstream = payload['logStream']
    lambda_func_name = loggroup.split('/')
    for log_event in log_events:
        error_msg.append( json.loads(log_event['message'].split('web:',1)[1]))
    return loggroup, logstream, error_msg, lambda_func_name

def send_slack(json_object):
    slack_message = {
    "channel": SLACK_CHANNEL,
    "attachments": [{
      "color": "#eb4034",
      "blocks": [
      {
        "type": "context",
        "elements": [
            {
                "type": "mrkdwn",
                "text": json_object['message']
            }
        ]
      },
        {
          "type": "section",
          "fields": [
               {
                  "type": "mrkdwn",
                  "text": "발생 시간: " + json_object['time']
              },
              {
                  "type": "mrkdwn",
                  "text": "USER_ID: " + json_object['userId']
              }

          ]
        }
      ]
    }],
    "blocks": [
      {
        "type": "header",
        "text": {
            "type": "plain_text",
            "text": "[Gradnet 에러알림] " + json_object['errCode']
        }
      },
      {
        "type": "divider"
      }
    ]}

    req = Request(HOOK_URL, json.dumps(slack_message).encode('utf-8'))
    try:
        response = urlopen(req)
        response.read()
    except HTTPError as e:
        logger.error("Request failed: %d %s", e.code, e.reason)
    except URLError as e:
        logger.error("Server connection failed: %s", e.reason)

    

def lambda_handler(event, context):

    pload = logpayload(event)
    lgroup, lstream, error_msg, lambdaname = error_details(pload)
    for msg in error_msg:
        send_slack(msg)



   
```



## CloudWatch

> 작업  -> 구독필터 -> Lambda 구독필터 생성



```
{$.errCode= "*결제 후처리 실패*" }
```

