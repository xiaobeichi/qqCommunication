import requests
from bs4 import BeautifulSoup as bt

url = 'https://blog.csdn.net/c910118/article/details/79022169'
#url = 'http://httpbin.org/get'
headers = {'User-Agent':"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36"}
data = requests.get(url,headers = headers)
print(data.text)

#解析数据
soup = bt(data.text,'lxml')
print(soup)
