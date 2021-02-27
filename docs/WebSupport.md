# 🤖 Play against the RoyalUrAnalysis AI's on the web!
As well as using the AI's in this to analyse the strategy in The Royal
Game of Ur, they can also be used as formidable opponents to play against
on the web! This is achieved through compiling this project to WASM
(Web Assembly). RoyalUrAnalysis can then be loaded using its JavaScript API
by web apps so that they can request moves from the RoyalUrAnalysis AI's.

Currently, the API through WASM only allows for requesting moves from
the [Depth-7 Expectimax Agent](/docs/Agents.md#-the-expectimax-agent-),
but in the future more agents are intended to be supported through the API.


# 🏁 Compiling to WASM
RoyalUrAnalysis has a special Maven profile for compiling to WASM,
which can be invoked through:
```
mvn package -Pwasm
```
The compiled WASM program will then be generated under `/target/bytecoder/`.


# 🪝 RoyalUrAnalysis JavaScript API
The generated `/target/bytecoder/api.js` file contains the JavaScript API
that you can use to interact with RoyalUrAnalysis. The source of this
API can be found in [api.js](/src/main/java/com/sothatsit/royalur/browser/api.js).

### Start RoyalUrAnalysis
You can load and start the RoyalUrAnalysis program through a call to
`royalUrAnalysis.load(sourcePath, onLoad, onError)`.

`sourcePath`: This should be the path where the
/target/bytecoder/royal_ur_analysis.wasm file can be found.

`onLoad`: An optional callback that will be called once
RoyalUrAnalysis has loaded.

`onError`: An optional callback that will be called if
there are any errors loading RoyalUrAnalysis.

**Example:**
```
royalUrAnalysis.load(
    "/game/royal_ur_analysis.wasm",
    () => console.log("RoyalUrAnalysis loaded!"),
    error => console.error("There was an error loading RoyalUrAnalysis: " + error)
);
```

### Send requests to RoyalUrAnalysis
Once RoyalUrAnalysis has been loaded, you can send it requests using
`royalUrAnalysis.sendRequest(request)`.

`request`: A string containing the encoded request for RoyalUrAnalysis.

`returns`: A string containing the encoded response from RoyalUrAnalysis.

**Example:**
```
const response = royalUrAnalysis.sendRequest(request); 
```

### Format of requests and responses
The format of the requests and responses to and from RoyalUrAnalysis is subject to
change, and so it does not have any documentation yet. Your best bet is to check out
[WasmMain.java](/src/main/java/com/sothatsit/royalur/browser/WasmMain.java)
itself to see the format of the request and responses.


# 🏆 Play the AI
Now that RoyalUrAnalysis is compiled to WASM, it can be loaded by any
Royal Game of Ur website and used as an opponent!

Currently the only known website with RoyalUrAnalysis support is https://royalur.net:
<p align="center"><a href="https://royalur.net">
  <img src="https://royalur.net/banner.jpg" />
</a></p>

If you know any other websites that hook into RoyalUrAnalysis, please
create a GitHub issue so that a link to them can be added here!
