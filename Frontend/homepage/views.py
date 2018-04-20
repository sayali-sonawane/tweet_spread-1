from django.shortcuts import render
from . import process_search
#from producer import produce
#import logging
#log = logging.getLogger(__name__)
import json

def index(request):
	world_json = json.load(open('homepage/world.json'))
	return render(request, 'homepage/world_map_template.html', {'world': world_json})


def home(request):
	if request.method == 'GET':
		query = request.GET['search']
		if query:
			process_search.connect_kafka(query)
			top_tweets_html = process_search.accept_input_for_processing(query)
			sentiment = process_search.get_sentiment()
			return render(request, 'homepage/search.html', {'query': query, 'top_tweets_html': top_tweets_html,
															'sentiment': sentiment})


