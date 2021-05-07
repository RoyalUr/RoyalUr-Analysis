//
// This file contains code to interact with the RoyalUrAnalysis program
// through JavaScript. It loads the WASM compiled version of RoyalUrAnalysis,
// and interacts with it through the use of functions exposed using Bytecoder.
//
// See Bytecoder: https://github.com/mirkosertic/Bytecoder
//

/** The object that handles interaction with the RoyalUrAnalysis program. **/
function RoyalUrAnalysisAPI() {
    this.__class_name__ = "RoyalUrAnalysisAPI";
    this.bytecoder = bytecoder;

    this.loading = false;
    this.loaded = false;
    this.errored = false;
    this.error = null;

    this.sourcePath = null;
    this.onLoad = null;
    this.onError = null;

    this.lastRequest = null;
}
/** Sends a request to the RoyalUrAnalysis program. **/
RoyalUrAnalysisAPI.prototype.sendRequest = function(request) {
    if (!this.loaded)
        throw "The RoyalUrAnalysis program has not been loaded!"

    this.lastRequest = this.bytecoder.toBytecoderString(request);
    return this.bytecoder.toJSString(bytecoder.exports.handleRequest());
};
/** Initialises and starts the RoyalUrAnalysis program. **/
RoyalUrAnalysisAPI.prototype.initialiseRoyalUrAnalysis = function(result) {
    this.bytecoder.init(result.instance);
    this.bytecoder.exports.initMemory(0);
    this.bytecoder.exports.bootstrap(0);
    this.bytecoder.initializeFileIO();
    this.bytecoder.exports.main(0);
    this.startGarbageCollector();

    this.loaded = true;
    if (this.onLoad) {
        this.onLoad();
    }
};
/** We have to manually call the GC for the Bytecoder WASM JRE. **/
RoyalUrAnalysisAPI.prototype.startGarbageCollector = function() {
    const gcInterval = 200,
          gcMaxObjectsPerRun = 30;

    setInterval(function() {
        bytecoder.exports.IncrementalGC(0, gcMaxObjectsPerRun);
    }, gcInterval);
};
/** Called when RoyalUrAnalysis fails to load. **/
RoyalUrAnalysisAPI.prototype.onLoadErrored = function(error) {
    this.errored = true;
    this.error = error;
    console.error("[RoyalUrAnalysisAPI] Failed to load RoyalUrAnalysis: " + error)

    if (this.onError) {
        this.onError(error);
    }
};
/** Starts loading the RoyalUrAnalysis program. **/
RoyalUrAnalysisAPI.prototype.load = function(sourcePath, onLoad, onError) {
    if (this.loaded)
        throw "RoyalUrAnalysis is already loaded";
    if (this.loading)
        throw "RoyalUrAnalysis is already loading";
    this.errored = false;
    this.error = null;

    this.sourcePath = sourcePath;
    this.onLoad = (onLoad ? onLoad : null);
    this.onError = (onError ? onError : null);

    // Try to load the WASM file of RoyalUrAnalysis.
    const initFn = this.initialiseRoyalUrAnalysis.bind(this),
          errorFn = this.onLoadErrored.bind(this);

    try {
        const request = new XMLHttpRequest();
        request.open('GET', this.sourcePath);
        request.responseType = 'arraybuffer';
        request.send();

        request.onload = function() {
            try {
                WebAssembly.instantiate(request.response, bytecoder.imports)
                    .then(initFn)
                    .catch(errorFn);
            } catch (e) {
                errorFn(e);
            }
        };
        request.onerror = errorFn;
    } catch (e) {
        errorFn(e);
    }
};

// Create the API object.
const royalUrAnalysis = new RoyalUrAnalysisAPI();
bytecoder.imports.royalUrAnalysis = {
    getLastRequest: () => royalUrAnalysis.lastRequest
};
