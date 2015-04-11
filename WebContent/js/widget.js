var WIDGET = WIDGET || (function(){
    var _args = {}; // private

    return {
    	// of course this is a local working example, once it's on server we have to configure jquery src to an absolute path
    	// and configure a cross domain request from the server to allow data retrieval from another server.
    	// profile.html link  should be absolute as well :)
        init : function(Args) {
            _args = Args;
            // if jQuery hasn not be loaded
            if(!window.jQuery)
            {
            var script = document.createElement('script');
            script.src = 'js/jquery-2.1.1.min.js';
            document.getElementsByTagName('head')[0].appendChild(script);
            }
        },
        GetMessages : function() {
        	
        	// full ajax call url path is requiered when app is live
        	$.ajax({
		        url: "/MicroBlog/user/GetUserMessages/name/"+_args[0],
		        type: 'GET',
		        contentType: "application/json; charset=utf-8",
		        success: function(msgsArray) {
		        	for(var i = 0 ; i< msgsArray.length ; i++){
		        		document.write("<div class='media'>");
		        		document.write("<div class='media-body'>");
		        		document.write("<h4 class='media-heading'>");
		        		document.write("<b><a href='profile.html?name="+ msgsArray[i].Posted_by_Nickname
		        				+ "'>@"+ msgsArray[i].Posted_by_Nickname +"</a></b>");
		        		document.write(msgsArray[i].pubDate + "</h4>");
		        		document.write("<p>"+ msgsArray[i].Content +"</p>");
		        		document.write("</div><hr></div>");
		        	}
		        }
		    	});
        }
    };
}());


/*
** This will be embeded at sites **
<script type="text/javascript" src="js/widget.js"></script>
<script type="text/javascript">
   WIDGET.init(["jawad"]);
   WIDGET.GetMessages();
</script>

*/