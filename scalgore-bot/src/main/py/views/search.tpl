<html>
 <head>
  <title>Freenode/#Akka Archives</title>
 </head>
 <body>
 <div align="top">
    <form method="GET">
        <input type="text" size="50" name="search" id="search"/>
        <input type="submit"/>
    </form>
 </div>
 <div id="results">
    %if results['hits']['total']:
        <h3>First 100 of {{results['hits']['total']}} results for query.</h3>
        %for entry in sorted([x['_source'] for x in results['hits']['hits']], key=lambda n: n.get("date", ""), reverse=True):
            <p>{{entry.get('date', '')}} &lt;<a href="/akka/search?user={{entry['sender']}}">{{entry['sender']}}</a>&gt; {{entry['message']}}</p>
        %end
    %else:
        No results.
    %end
 </div>
 </body>
</html>
