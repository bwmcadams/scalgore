#!/usr/bin/python
from bottle import *
from pyelasticsearch import *

conn = ElasticSearch("http://localhost:9200")

@route("/akka/search")
@view('search')
def search(): 
    users = dict(map(lambda x: (x['term'], x['doc_freq']), conn.terms(["sender"])['fields']['sender']['terms']))
    if 'search' in request.GET:
        query = request.GET['search']
        results = conn.search(query)
        return dict(results=results, users=users)
    return dict(users=users)
        
#run(server=FlupServer)
run(host='localhost', port=8080)
