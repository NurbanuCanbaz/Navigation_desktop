package com.project.navigation;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.addOnMapClickListener;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;
import static com.mapbox.navigation.base.extensions.RouteOptionsExtensions.applyDefaultNavigationOptions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.directions.v5.models.VoiceInstructions;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.api.matrix.v1.MapboxMatrix;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.bindgen.Expected;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentConstants;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.base.route.NavigationRoute;
import com.mapbox.navigation.base.route.NavigationRouterCallback;
import com.mapbox.navigation.base.route.RouterFailure;
import com.mapbox.navigation.base.route.RouterOrigin;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.directions.session.RoutesObserver;
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult;
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp;
import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
import com.mapbox.navigation.core.trip.session.LocationObserver;
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver;
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer;
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView;
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineError;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources;
import com.mapbox.navigation.ui.maps.route.line.model.RouteSetValue;
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi;
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer;
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement;
import com.mapbox.navigation.ui.voice.model.SpeechError;
import com.mapbox.navigation.ui.voice.model.SpeechValue;
import com.mapbox.navigation.ui.voice.model.SpeechVolume;
import com.mapbox.navigation.ui.voice.view.MapboxSoundButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.turf.TurfMeasurement;
import com.project.navigation.activities.MainActivity;
import com.project.navigation.activities.SignInActivity;

public class LifetimeOriginal extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private Point orig;
    private Button alarmOnOf, Return;
    MapView mapView;
    //MaterialButton setRoute;
    private FirebaseAuth.AuthStateListener mAuthListener;
    double distance, Speed;
    private MediaPlayer mp;
    FloatingActionButton focusLocationBtn;
    private final NavigationLocationProvider navigationLocationProvider = new NavigationLocationProvider();
    private MapboxRouteLineView routeLineView;
    private MapboxRouteLineApi routeLineApi;
    private final LocationObserver locationObserver = new LocationObserver() {
        @Override
        public void onNewRawLocation(@NonNull Location location) {

        }

        @Override
        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
            Location location = locationMatcherResult.getEnhancedLocation();
            navigationLocationProvider.changePosition(location, locationMatcherResult.getKeyPoints(), null, null);
            if (focusLocation) {
                updateCamera(Point.fromLngLat(location.getLongitude(), location.getLatitude()), (double) location.getBearing());
            }
        }
    };
    private final RoutesObserver routesObserver = new RoutesObserver() {
        @Override
        public void onRoutesChanged(@NonNull RoutesUpdatedResult routesUpdatedResult) {
            routeLineApi.setNavigationRoutes(routesUpdatedResult.getNavigationRoutes(), new MapboxNavigationConsumer<Expected<RouteLineError, RouteSetValue>>() {
                @Override
                public void accept(Expected<RouteLineError, RouteSetValue> routeLineErrorRouteSetValueExpected) {
                    Style style = mapView.getMapboxMap().getStyle();
                    if (style != null) {
                        routeLineView.renderRouteDrawData(style, routeLineErrorRouteSetValueExpected);
                    }
                }
            });
        }
    };
    boolean focusLocation = true;
    private MapboxNavigation mapboxNavigation;

    private void updateCamera(Point point, Double bearing) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder().duration(1500L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(18.0).bearing(bearing).pitch(45.0)
                .padding(new EdgeInsets(1000.0, 0.0, 0.0, 0.0)).build();

        getCamera(mapView).easeTo(cameraOptions, animationOptions);
    }

    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            focusLocation = false;
            getGestures(mapView).removeOnMoveListener(this);
            focusLocationBtn.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };
    private final ActivityResultLauncher<String> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
        @Override
        public void onActivityResult(Boolean result) {
            if (result) {
                Toast.makeText(LifetimeOriginal.this, "Permission granted! Restart this app", Toast.LENGTH_SHORT).show();
            }
        }
    });
    /*

    private MapboxSpeechApi speechApi;
    private MapboxVoiceInstructionsPlayer mapboxVoiceInstructionsPlayer;

    private MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> speechCallback = new MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>>() {
        @Override
        public void accept(Expected<SpeechError, SpeechValue> speechErrorSpeechValueExpected) {
            speechErrorSpeechValueExpected.fold(new Expected.Transformer<SpeechError, Unit>() {
                @NonNull
                @Override
                public Unit invoke(@NonNull SpeechError input) {
                    mapboxVoiceInstructionsPlayer.play(input.getFallback(), voiceInstructionsPlayerCallback);
                    return Unit.INSTANCE;
                }
            }, new Expected.Transformer<SpeechValue, Unit>() {
                @NonNull
                @Override
                public Unit invoke(@NonNull SpeechValue input) {
                    mapboxVoiceInstructionsPlayer.play(input.getAnnouncement(), voiceInstructionsPlayerCallback);
                    return Unit.INSTANCE;
                }
            });
        }
    };

    private MapboxNavigationConsumer<SpeechAnnouncement> voiceInstructionsPlayerCallback = new MapboxNavigationConsumer<SpeechAnnouncement>() {
        @Override
        public void accept(SpeechAnnouncement speechAnnouncement) {
            speechApi.clean(speechAnnouncement);
        }
    };

    VoiceInstructionsObserver voiceInstructionsObserver = new VoiceInstructionsObserver() {
        @Override
        public void onNewVoiceInstructions(@NonNull VoiceInstructions voiceInstructions) {
            speechApi.generate(voiceInstructions, speechCallback);
        }
    };

    private boolean isVoiceInstructionsMuted = false;*/
    private TextView remainingTime, remainingDistance;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifetime_original);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mapView = findViewById(R.id.mapView);
        focusLocationBtn = findViewById(R.id.focusLocation);
        //setRoute = findViewById(R.id.setRoute);
        remainingTime = findViewById(R.id.remainingTime);
        remainingDistance = findViewById(R.id.remainingDistance);
        alarmOnOf = findViewById(R.id.alarmOnOf);

        SharedPreferences preferences = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        // Use getString with the resource id as default value
        String defaultAddress = getString(R.string.default_address);
        String address = preferences.getString("address", defaultAddress);
        if(address!=null){
            Toast.makeText(LifetimeOriginal.this, "address: "+ address, Toast.LENGTH_SHORT).show();
            geocodeAddress(address);

        }


        MapboxRouteLineOptions options = new MapboxRouteLineOptions.Builder(this).withRouteLineResources(new RouteLineResources.Builder().build())
                .withRouteLineBelowLayerId(LocationComponentConstants.LOCATION_INDICATOR_LAYER).build();
        routeLineView = new MapboxRouteLineView(options);
        routeLineApi = new MapboxRouteLineApi(options);

        //speechApi = new MapboxSpeechApi(LifetimeOriginal.this, getString(R.string.mapbox_access_token), Locale.US.toLanguageTag());
        //mapboxVoiceInstructionsPlayer = new MapboxVoiceInstructionsPlayer(LifetimeOriginal.this, Locale.US.toLanguageTag());

        NavigationOptions navigationOptions = new NavigationOptions.Builder(this).accessToken(getString(R.string.mapbox_access_token)).build();

        MapboxNavigationApp.setup(navigationOptions);
        mapboxNavigation = new MapboxNavigation(navigationOptions);

        mapboxNavigation.registerRoutesObserver(routesObserver);
        mapboxNavigation.registerLocationObserver(locationObserver);
        //mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(LifetimeOriginal.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (ActivityCompat.checkSelfPermission(LifetimeOriginal.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(LifetimeOriginal.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            activityResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            mapboxNavigation.startTripSession();
        }

        focusLocationBtn.hide();
        LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
        getGestures(mapView).addOnMoveListener(onMoveListener);

        /*
        setRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LifetimeOriginal.this, "Please select a location in map", Toast.LENGTH_SHORT).show();
            }
        });*/

        mapView.getMapboxMap().loadStyleUri(Style.TRAFFIC_DAY, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build());
                locationComponentPlugin.setEnabled(true);
                locationComponentPlugin.setLocationProvider(navigationLocationProvider);
                getGestures(mapView).addOnMoveListener(onMoveListener);
                locationComponentPlugin.updateSettings(new Function1<LocationComponentSettings, Unit>() {
                    @Override
                    public Unit invoke(LocationComponentSettings locationComponentSettings) {
                        locationComponentSettings.setEnabled(true);
                        locationComponentSettings.setPulsingEnabled(true);
                        return null;
                    }
                });
                focusLocationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        focusLocation = true;
                        getGestures(mapView).addOnMoveListener(onMoveListener);
                        focusLocationBtn.hide();
                    }
                });
            }
        });

    }

    private Point userPoint;
    private void geocodeAddress(String address) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_pin);
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);
        // Set your Mapbox access token here
        String accessToken = "sk.eyJ1IjoiemV5Z25jeSIsImEiOiJjbHdvbzNrdzYxMGJyMmpyejJ5cjc1YWlhIn0.oj2CyXZbYL6BKDEkctFXAg";

        MapboxGeocoding mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken(accessToken)
                .query(address)
                .build();

        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(@NonNull Call<GeocodingResponse> call, @NonNull Response<GeocodingResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CarmenFeature feature = response.body().features().get(0);
                    Point point = feature.center();
                    double latitude = point.latitude();
                    double longitude = point.longitude();
                    Point e = Point.fromLngLat(longitude,latitude);
                    //userPoint = e;
                    PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                            .withTextAnchor(TextAnchor.CENTER)
                            .withIconImage(bitmap)
                            .withPoint(e);
                    pointAnnotationManager.create(pointAnnotationOptions);

                    fetchRoute(e);

                    // Save the address and coordinates to the database

                } else {
                    //Toast.makeText(MatrixPage.this, "No location found for the given address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<GeocodingResponse> call, @NonNull Throwable t) {
               // Toast.makeText(MatrixPage.this, "Geocoding request failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressLint("MissingPermission")
    private double calculateSpeed(){
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(LifetimeOriginal.this);
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                Location locations = result.getLastLocation();

                Speed = result.getLastLocation().getSpeed();
                //Toast.makeText(LifetimeOriginal.this, "Speed: " +Speed, Toast.LENGTH_SHORT).show();
                //Toast.makeText(LifetimeOriginal.this, "Origin: " +orig, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
        return Speed;
    }


    private void calculateDistance(Point origin, Point destination) {

        String accessToken = "sk.eyJ1IjoiemV5Z25jeSIsImEiOiJjbHdvbzNrdzYxMGJyMmpyejJ5cjc1YWlhIn0.oj2CyXZbYL6BKDEkctFXAg";
        List<Point> pairPoints= new ArrayList<>();
        pairPoints.add(origin);
        pairPoints.add(destination);
        // Make a request to the Mapbox Matrix API
        MapboxMatrix mapboxMatrix = MapboxMatrix.builder()
                .accessToken(accessToken)
                .profile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
                .coordinates(pairPoints)
                .build();
        mapboxMatrix.enqueueCall(new Callback<MatrixResponse>() {
            @Override
            public void onResponse(Call<MatrixResponse> call, Response<MatrixResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MatrixResponse matrixResponse = response.body();
                    Log.e("MatrixAPI", "Response Body: " + response.body().toString());
                    // Check if destinations and durations are not null and not empty
                    if (matrixResponse.destinations() != null && !matrixResponse.destinations().isEmpty() &&
                            matrixResponse.durations() != null && !matrixResponse.durations().isEmpty()) {

                        double distanceBetweenLastAndSecondToLastClickPoint = 0;
                        double totalLineDistance = 0;

                        // Make the Turf calculation between the last tap point and the second-to-last tap point.
                        if (pairPoints.size() >= 2) {
                            distanceBetweenLastAndSecondToLastClickPoint = TurfMeasurement.distance(
                                    pairPoints.get(pairPoints.size() - 2), pairPoints.get(pairPoints.size() - 1));
                            totalLineDistance += distanceBetweenLastAndSecondToLastClickPoint;
                        }
                        distanceBetweenLastAndSecondToLastClickPoint=distanceBetweenLastAndSecondToLastClickPoint*1.609344;
                        totalLineDistance = (totalLineDistance*1.609344);
                        String distanceFormattedValue = String.format("%.2f", totalLineDistance);

                        Location location = new Location("point_to_location");
                        location.setLatitude(origin.latitude());
                        location.setLongitude(origin.longitude());
                        //fetchRoute(pairPoints);
                        Speed = calculateSpeed();
                        if((Speed <= 5.0) ){
                            Speed=30.0;
                        }
                        else{
                            Speed = Speed*1.609344;
                        }
                        Double time=(totalLineDistance/Speed);
                        time = time * 60;
                        String timeFormattedValue = String.format("%.2f", time);
                        String cleanedMinutesStr = timeFormattedValue.replaceAll(",", "");
                        cleanedMinutesStr= cleanedMinutesStr.substring(0, cleanedMinutesStr.length() - 2);

                        // Parse the cleaned string to integer
                        int minutesToAdd = Integer.parseInt(cleanedMinutesStr);

                        Date currentTime = Calendar.getInstance().getTime();

                        // Add the specified number of minutes to the current time
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(currentTime);
                        calendar.add(Calendar.MINUTE, minutesToAdd);
                        Date updatedTime = calendar.getTime();
                        //String newTime = 1+timeDisplay();
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                        String formattedTime = timeFormat.format(updatedTime);

                        remainingDistance.setText("Remaining Distance: \n" + distanceFormattedValue+ " km");
                        remainingTime.setText("Remaining Time: \n" + timeFormattedValue+ " min");
                        if (totalLineDistance <= 1) {  // aralarındaki mesafe 1 km kalınca yani yaklaşık 10 -12dk arasında olunca alarm çalacak
                            //set alarmm1!!!!!!!!!!!

                            mp = MediaPlayer.create(LifetimeOriginal.this, R.raw.alarmvoice);
                            mp.setLooping(true);
                            mp.start();
                            alarmOnOf.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mp.setLooping(false);
                                    mp.stop();

                                }
                            });

                        } else {
                            //Toast.makeText(AddressActivity.this, "Address not found for user ID: " + userId, Toast.LENGTH_SHORT).show();
                        }
                        //Double time=(totalLineDistance/Speed);
                        //Location location = new Location("point_to_location");
                        //location.setLatitude(origin.latitude());
                        //location.setLongitude(origin.longitude());
                        //fetchRoute(pairPoints);

                    } else {
                        // Display a message if destinations or durations are empty
                        Toast.makeText(LifetimeOriginal.this, "Empty response for destinations or durations", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the error message if the response is not successful
                    Log.e("MatrixAPI", "Failed to get a successful response. Error message: " + response.message());
                    // Display a message if the response is not successful
                    Toast.makeText(LifetimeOriginal.this, "Failed to get a successful response", Toast.LENGTH_SHORT).show();
                }
            }   @Override
            public void onFailure(Call<MatrixResponse> call, Throwable t) {
                Toast.makeText(LifetimeOriginal.this, "Failed to calculate distance: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void fetchRoute(Point point) {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(LifetimeOriginal.this);
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                Location location = result.getLastLocation();
                //setRoute.setEnabled(false);
                //setRoute.setText("Fetching route...");
                RouteOptions.Builder builder = RouteOptions.builder();
                Point origin = Point.fromLngLat(Objects.requireNonNull(location).getLongitude(), location.getLatitude());
                builder.coordinatesList(Arrays.asList(origin, point));
                builder.alternatives(false);
                builder.profile(DirectionsCriteria.PROFILE_DRIVING);
                builder.bearingsList(Arrays.asList(Bearing.builder().angle(location.getBearing()).degrees(45.0).build(), null));
                applyDefaultNavigationOptions(builder);

                mapboxNavigation.requestRoutes(builder.build(), new NavigationRouterCallback() {
                    @Override
                    public void onRoutesReady(@NonNull List<NavigationRoute> list, @NonNull RouterOrigin routerOrigin) {
                        mapboxNavigation.setNavigationRoutes(list);
                        focusLocationBtn.performClick();
                        //setRoute.setEnabled(true);
                        //setRoute.setText("Set route");
                    }

                    @Override
                    public void onFailure(@NonNull List<RouterFailure> list, @NonNull RouteOptions routeOptions) {
                        //setRoute.setEnabled(true);
                        //setRoute.setText("Set route");
                        Toast.makeText(LifetimeOriginal.this, "Route request failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCanceled(@NonNull RouteOptions routeOptions, @NonNull RouterOrigin routerOrigin) {

                    }
                });
                calculateDistance(origin,point);
            }

            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapboxNavigation.onDestroy();
        mapboxNavigation.unregisterRoutesObserver(routesObserver);
        mapboxNavigation.unregisterLocationObserver(locationObserver);
    }

    @Override
    public void onBackPressed() {
        // do something on back.
        super.onBackPressed();
        startActivity(new Intent(LifetimeOriginal.this, Dashboard.class));
        return;
    }
}