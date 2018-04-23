// implementation of AR-Experience (aka "World")
var World = {
	// true once data was fetched
	initiallyLoadedData: false,
/*	var poiData = {
				"id": 1,
				"longitude": (13.0538109 + (Math.random() / 5 - 0.1)),
				"latitude": (74.9722021 + (Math.random() / 5 - 0.1)),
				"altitude": 13
			};,
	// POI-Marker asset
	markerDrawable_idle: null,
init: function initFn() {
		World.loadPoisFromJsonData(poiData);
	},*/
	// called to inject new POI data
	loadPoisFromJsonData: function loadPoisFromJsonDataFn(poiData) {

		/*
			The example Image Recognition already explained how images are loaded and displayed in the augmented reality view. This sample loads an AR.ImageResource when the World variable was defined. It will be reused for each marker that we will create afterwards.
		*/
		World.markerDrawable_idle = new AR.ImageResource("assets/marker_idle.png");

		/*
			For creating the marker a new object AR.GeoObject will be created at the specified geolocation. An AR.GeoObject connects one or more AR.GeoLocations with multiple AR.Drawables. The AR.Drawables can be defined for multiple targets. A target can be the camera, the radar or a direction indicator. Both the radar and direction indicators will be covered in more detail in later examples.
		*/
		var markerLocation = new AR.GeoLocation(poiData.latitude, poiData.longitude, poiData.altitude);
		//var markerLocation = new AR.GeoLocation(13.0538109,74.9722021,13);

		var markerImageDrawable_idle = new AR.ImageDrawable(World.markerDrawable_idle, 2.5, {
			zOrder: 0,
			opacity: 1.0
		});

		// create GeoObject
		var markerObject = new AR.GeoObject(markerLocation, {
			drawables: {
				cam: [markerImageDrawable_idle]
			}
		});

		// Updates status message as a user feedback that everything was loaded properly.
		World.updateStatusMessage('1 place loaded @'+ markerLocation.latitude + 'and' + poiData.longitude);
	},

	// updates status message shown in small "i"-button aligned bottom center
	updateStatusMessage: function updateStatusMessageFn(message, isWarning) {

		var themeToUse = isWarning ? "e" : "c";
		var iconToUse = isWarning ? "alert" : "info";

		$("#status-message").html(message);
		$("#popupInfoButton").buttonMarkup({
			theme: themeToUse
		});
		$("#popupInfoButton").buttonMarkup({
			icon: iconToUse
		});
	},

	// location updates, fired every time you call architectView.setLocation() in native environment
	locationChanged: function locationChangedFn(lat, lon, alt, acc) {

		/*
			The custom function World.onLocationChanged checks with the flag World.initiallyLoadedData if the function was already called. With the first call of World.onLocationChanged an object that contains geo information will be created which will be later used to create a marker using the World.loadPoisFromJsonData function.
		*/
		if (!World.initiallyLoadedData) {
			// creates a poi object with a random location near the user's location
			var poiData = {
				"id": 1,
				//"longitude": (74.89088584566348),
				//"latitude": ( 13.128628403018228),
				"longitude":(74.8908858 + (Math.random() / 5-0.1)),
				"latitude":(13.1286284+(Math.random() / 5-0.1)),
				"altitude": 16.0
			};

			World.loadPoisFromJsonData(poiData);
			World.initiallyLoadedData = true;
		}
	},
};
7
/* 
	Set a custom function where location changes are forwarded to. There is also a possibility to set AR.context.onLocationChanged to null. In this case the function will not be called anymore and no further location updates will be received. 
*/
AR.context.onLocationChanged = World.locationChanged;
//World.init();