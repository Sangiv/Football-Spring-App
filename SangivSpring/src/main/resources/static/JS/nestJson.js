fetch('http://localhost:8901/club/readAll/')
  .then(
    function(response) {
      if (response.status !== 200) {
        console.log('Looks like there was a problem. Status Code: ' +
          response.status);
        return;
      }

      // Examine the text in the response
      response.json().then(function(data) {
        console.log(data);
        for(let playerRecord of data) {
            console.log("here is my player record: ",playerRecord.players);
            for(let singleRecord in playerRecord.players) {
                console.log("here is the single record: ",playerRecord.players[singleRecord]);
            }
        }
      });
    }
  )
  .catch(function(err) {
    console.log('Fetch Error :-S', err);
  });