import urllib.request
import time
import os.path

STOCKS_FILE_NAME = 'stocks.tsv'
BASE_URL = 'https://www.google.com/finance/historical?q={}&startdate=Jan+01%2C+2010&enddate=Dec+31%2C+2016&output=csv'
DOWNLOAD_FOLDER = 'day/'

# Open file
try:
    file = open(STOCKS_FILE_NAME)
except FileNotFoundError:
    exit('File %s could not be found' % STOCKS_FILE_NAME)

# Create folder if not exists
if not os.path.exists(DOWNLOAD_FOLDER):
    os.makedirs(DOWNLOAD_FOLDER)

# Read file line by line and download each csv
for line in file.readlines():
    try:
        splitted = line.split('\t')

        if splitted[0] == 'Exchange':
            continue  # ignore first line

        exchange = splitted[0]
        exchange_api = exchange[:]  # Google Finance API uses other exchange acronyms
        symbol = splitted[1]
        if exchange == 'TSX':
            exchange_api = 'TSE'
            symbol = symbol[:-3]  # remove .TO
        if exchange == 'XETRA':
            symbol = symbol[:-3]  # remove .DE
        if exchange == 'HKSE':
            symbol = symbol[:-3]  # remove .HK
        if exchange == 'BSE':
            symbol = symbol[:-3]  # remove .BO
        if exchange == 'ASX':
            symbol = symbol[:-3]  # remove .AS
        if exchange == 'LSE':
            exchange_api = 'LON'
            symbol = symbol[:-2]  # remove .L

        save_file_name = '{0}_{1}.csv'.format(exchange, symbol)

        if os.path.isfile(DOWNLOAD_FOLDER + save_file_name):
            continue  # ignore already downloaded files

        try:
            url = BASE_URL.format(exchange_api + ':' + symbol)
            csv = urllib.request.urlretrieve(url, DOWNLOAD_FOLDER + save_file_name)

            print('Saved %s' % save_file_name)
            time.sleep(1)  # to prevent IP block
        except urllib.request.URLError:
            print('Could not retrieve %s' % url)

    except Exception as e:
        print('Could not read line %s' % line)
