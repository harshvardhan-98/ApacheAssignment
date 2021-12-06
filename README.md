**Problem3: Given CSV file of  movie tags data(find attached movie_tags.csv file)**


Write a Beam pipeline to read the movie_tag.csv file and compute the count of movies with a particular tag has been seen by User(UserId). Save the output in another file with Header [userId,movie_tag,tag_count]
Output sample file-

userId,movie_tag,tag_count
18,Alpacino,2
18,gangster,1
2,funny,1
2,drugs,1


<br>
**Given CSV file of sales data(find attached SalesJan2009.csv file)**


Write a Beam pipeline to read the given CSV file of sales data and compute the total sale of every day of Jan 2009. Save the output in another CSV file with headers [date,total_sale].
date,total_sale-
01,7200
02,3600
03,4500
