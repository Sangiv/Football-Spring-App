const params = new URLSearchParams(window.location.search);
    
for(const param of params){
  console.log(param);
  let id = param[1];
  console.log(id);
  readOne(id);
}

function readOne(id){
    fetch('http://localhost:8901/player/readOne/'+id)
    .then(
      function(response) {
        if (response.status !== 200) {
          console.log('Looks like there was a problem. Status Code: ' +
            response.status);
          return;
        }
  
        // Examine the text in the response
        response.json().then(function(playerRecord) {
          console.log(playerRecord);
          document.getElementById("inputID").value=playerRecord.id;
          document.getElementById("inputName").value=playerRecord.name;
          document.getElementById("inputPosition").value=playerRecord.position;
        });
      }
    )
    .catch(function(err) {
      console.log('Fetch Error :-S', err);
    });
}

document.querySelector("form.playerForm").addEventListener("submit", function (stop) {
    stop.preventDefault();

    let formElements = document.querySelector("form.playerForm").elements;
     console.log(formElements);
    let id=formElements["inputID"].value;
    let name=formElements["inputName"].value;
    let position=formElements["inputPosition"].value;

    updatePlayer(id,name,position)
  });

  function updatePlayer(id,name,position){
    fetch("http://localhost:8901/player/update/"+id, {
        method: 'put',
        headers: {
          "Content-type": "application/json"
        },
        body: json = JSON.stringify({
            "id": id,
            "name": name,
            "position": position,
        })
      })
      .then(json)
      .then(function (data) {
        console.log('Request succeeded with JSON response', data);
      })
      .catch(function (error) {
        console.log('Request failed', error);
      });
  }