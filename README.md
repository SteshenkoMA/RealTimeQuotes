# RealTimeQuotes
Данная программа отображает котировки акций c yahoo.finance.com c 10 секундной задержкой.

• Поддерживается подмигивание клетки с ценой при обновлении значения котировки.  
• Числовые данные округляются и окрашиваются в красный/зеленый цвет  
• Возможно посмотреть внутридневной график с дополнительной информацией по тикеру  
• Присутствует автозаполнение при вводе символа  

Используемые библиотеки:  
• org.json – для работы с форматом данных JSON  

Подробное описание:

RealTimeQuotes отображает котировки ценных бумаг с 10 секундой задержкой. Данные берутся с двух url, для отображения последней цены и дополнительной информации о ценной бумаге соответственно. Данные котировок представлены в формате JSON, и для работы с ними использована библиотека org.json. Котировки ценных бумаг отображаются в виде таблицы, которая поддерживает мигание ячейки с ценой (красным/зеленым) цветом в зависимости от значения новой котировки, кроме того остальные числовые значения (процентное изменение за день, изменение цены в пунктах) округляются и окрашиваются в цвет, соответствующий положительному или отрицательному значению. Для все этого используются специальные Renderers. Также в программе используется шаблон проектирования MVС. Для автообновления котировок используется класс Timer. Вывод внутридневного графика реализован при помощи отображения картинки, скачанной со специального url.

![1](https://cloud.githubusercontent.com/assets/13558216/11502403/128d7986-9854-11e5-99df-0694a45939bd.PNG)
![2](https://cloud.githubusercontent.com/assets/13558216/11502406/1526ecae-9854-11e5-94ab-9b8678cc71bb.PNG)

This program displays stock quotes from yahoo.finance.com with 10 second delay.

• Supports cells blinking when price is updating   
• Numerical data are rounded and colored in red/green  
• You can view intraday graph ticker with additional information  
• Autocomplete when you input a character  

Used libraries:  
• org.json – for JSON data format   

Detailed description:

RealTimeQuotes displays stock quotes with a 10 second delay. The data is taken from two URLs to display the latest prices and additional information respectively. These quotes are presented in JSON format, and to work with them the library org.json is used. Stock quotes are listed in the table that supports cell flashing of the price with (red/green) color depending on the values of the new quotes, in addition other numerical values (percentage change per day, price change in points) are rounded and painted in the color corresponding positive or negative value. For all this purpose special Renderers are used. Also the program uses a MVC design pattern. For auto-update quotes the Timer class is used. Intraday chart is implemented by displaying images that are downloaded from special url.


