#!/usr/bin/python
from bottle import *
from pyelasticsearch import *

conn = ElasticSearch("http://localhost:9200")

@route("/akka/search")
@view('search')
def search(): 
    users = dict(map(lambda x: (x['term'], x['doc_freq']), conn.terms(["sender"])['fields']['sender']['terms']))
    filter_user = ''
    search_term = ''
    if 'search' in request.GET:
        search_term = request.GET['search']
        print "Search: %s" % search_term
    
    if 'user' in request.GET:
        filter_user = request.GET['user']
        print "User: %s" % filter_user

    if filter_user and search_term:
        query = {
            'filtered': {
                'query': {'wildcard': {'message': search_term}
            },
            'filter': {
                'term':  {'sender': filter_user},
            }
          }
        }
    elif filter_user:
        query = {'term': {'sender': filter_user}}
    else:
        query = {'wildcard': {'message': search_term}}
    
    print query

    results = conn.search(None, body={'query': query}, indexes=['irclogs'], size=100)
    print results
    return dict(users=users, results=results)
        
run(server=FlupServer)
#run(host='localhost', port=8080)
