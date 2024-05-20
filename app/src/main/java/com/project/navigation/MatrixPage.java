package com.project.navigation;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.addOnMapClickListener;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;
import static com.mapbox.navigation.base.extensions.RouteOptionsExtensions.applyDefaultNavigationOptions;

import static java.lang.Double.valueOf;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.gestures.Constants;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.api.directions.v5.models.VoiceInstructions;
import com.mapbox.api.matrix.v1.MapboxMatrix;
import com.mapbox.api.matrix.v1.models.MatrixResponse;
import com.mapbox.bindgen.Expected;
import com.mapbox.geojson.FeatureCollection;
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
import com.mapbox.navigation.base.formatter.DistanceFormatterOptions;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.base.route.NavigationRoute;
import com.mapbox.navigation.base.route.NavigationRouterCallback;
import com.mapbox.navigation.base.route.RouterFailure;
import com.mapbox.navigation.base.route.RouterOrigin;
import com.mapbox.navigation.base.trip.model.RouteProgress;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.directions.session.RoutesObserver;
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult;
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter;
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp;
import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
import com.mapbox.navigation.core.trip.session.LocationObserver;
import com.mapbox.navigation.core.trip.session.RouteProgressObserver;
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver;
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer;
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi;
import com.mapbox.navigation.ui.maneuver.model.Maneuver;
import com.mapbox.navigation.ui.maneuver.model.ManeuverError;
import com.mapbox.navigation.ui.maneuver.view.MapboxManeuverView;
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider;
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi;
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView;
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions;
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
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion;
import com.mapbox.search.ui.adapter.autocomplete.PlaceAutocompleteUiAdapter;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import android.os.Handler;

public class MatrixPage extends AppCompatActivity {
    private DatabaseReference mDatabase;

    private FeatureCollection featureCollection;
    private List<Point> userPoint = new ArrayList<>();
    private RecyclerView recyclerView;
    private List<Point> searchedPoint=new ArrayList<>();
    private List<Point> destinationPoints = new ArrayList<>();

    private List<Point> coordinates = new ArrayList<>();
    private Location previousLocation;
    private double totalDistance;
    private long previousUpdateTimeMillis;


    private Double speed;
    MapView mapView;
    MaterialButton setRoute;
    FloatingActionButton focusLocationBtn;
    private final NavigationLocationProvider navigationLocationProvider = new NavigationLocationProvider();
    private MapboxRouteLineView routeLineView;
    private MapboxRouteLineApi routeLineApi;
    private Button Return;

    private TextView timeTextView;
    private Handler handler = new Handler();
    private Runnable runnable;


    private Point origin = Point.fromLngLat( 39.983748, 32.816814);
    private Point destination = Point.fromLngLat(40.056971, 32.646201);
    private final LocationObserver locationObserver = new LocationObserver() {


        @Override
        public void onNewRawLocation(@NonNull Location location) {
            if (previousLocation != null) {
                totalDistance += location.distanceTo(previousLocation);
            }
            previousLocation = location;

            // Calculate current speed
            long currentTimeMillis = System.currentTimeMillis();
            long timeElapsedMillis = currentTimeMillis - previousUpdateTimeMillis;
            double currentSpeed;
            if (timeElapsedMillis > 0) {
                currentSpeed = (location.distanceTo(previousLocation) / timeElapsedMillis) * 1000; // in meters per second
            } else {
                // If time elapsed is 0 or negative, set a default speed
                currentSpeed = 30.0; // Default speed: 5 meters per second
            }

            // Update UI with current speed
            updateSpeedUI(currentSpeed);

            previousUpdateTimeMillis = currentTimeMillis;
        }

        @Override
        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
            Location location = locationMatcherResult.getEnhancedLocation();
            navigationLocationProvider.changePosition(location, locationMatcherResult.getKeyPoints(), null, null);
            if (focusLocation) {
                updateCamera(Point.fromLngLat(location.getLongitude(), location.getLatitude()), (double) location.getBearing());
            }
        }

        private void updateSpeedUI(double currentSpeed) {

            double averageSpeed = totalDistance / (System.currentTimeMillis() - previousUpdateTimeMillis);

        }
    };

    private double calculateCurrentSpeed(Location location) {
        long currentTimeMillis = System.currentTimeMillis();
        long timeElapsedMillis = currentTimeMillis - previousUpdateTimeMillis;
        double currentSpeed;
        if (timeElapsedMillis > 0) {
            currentSpeed = (location.distanceTo(previousLocation) / timeElapsedMillis) * 1000; // in meters per second
        } else {
            // If time elapsed is 0 or negative, set a default speed
            currentSpeed = 5.0; // Default speed: 5 meters per second
        }
        previousUpdateTimeMillis = currentTimeMillis;
        return currentSpeed;
    }

    private String timeDisplay(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = timeFormat.format(currentTime);
        return formattedTime;

    }

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
                Toast.makeText(MatrixPage.this, "Permission granted! Restart this app", Toast.LENGTH_SHORT).show();
            }
        }
    });

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

    private boolean isVoiceInstructionsMuted = false;
    private EditText editTextOrigin;
    private EditText editTextDestination;
    private Button buttonCalculate;
    private TextView textViewResult,lngLt;
    private TextView timeCurrentResult;

    private PlaceAutocomplete placeAutocomplete;
    private SearchResultsView searchResultsView;
    private PlaceAutocompleteUiAdapter placeAutocompleteUiAdapter;
    private TextInputEditText searchET;
    private boolean ignoreNextQueryUpdate = false;
    private MapboxManeuverView mapboxManeuverView;
    private MapboxManeuverApi maneuverApi;
    private MapboxRouteArrowView routeArrowView;
    private MapboxRouteArrowApi routeArrowApi = new MapboxRouteArrowApi();
    private RouteProgressObserver routeProgressObserver = new RouteProgressObserver() {
        @Override
        public void onRouteProgressChanged(@NonNull RouteProgress routeProgress) {
            Style style = mapView.getMapboxMap().getStyle();
            if (style != null) {
                routeArrowView.renderManeuverUpdate(style, routeArrowApi.addUpcomingManeuverArrow(routeProgress));
            }

            maneuverApi.getManeuvers(routeProgress).fold(new Expected.Transformer<ManeuverError, Object>() {
                @NonNull
                @Override
                public Object invoke(@NonNull ManeuverError input) {
                    return new Object();
                }
            }, new Expected.Transformer<List<Maneuver>, Object>() {
                @NonNull
                @Override
                public Object invoke(@NonNull List<Maneuver> input) {
                    mapboxManeuverView.setVisibility(View.VISIBLE);
                    mapboxManeuverView.renderManeuvers(maneuverApi.getManeuvers(routeProgress));
                    return new Object();
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix_page);


        mapView = findViewById(R.id.mapView);
        focusLocationBtn = findViewById(R.id.focusLocation);
        setRoute = findViewById(R.id.setRoute);
        mapboxManeuverView = findViewById(R.id.maneuverView);
        Return = (Button)findViewById(R.id.Return);

        maneuverApi = new MapboxManeuverApi(new MapboxDistanceFormatter(new DistanceFormatterOptions.Builder(MatrixPage.this).build()));
        routeArrowView = new MapboxRouteArrowView(new RouteArrowOptions.Builder(MatrixPage.this).build());

        MapboxRouteLineOptions options = new MapboxRouteLineOptions.Builder(this).withRouteLineResources(new RouteLineResources.Builder().build())
                .withRouteLineBelowLayerId(LocationComponentConstants.LOCATION_INDICATOR_LAYER).build();
        routeLineView = new MapboxRouteLineView(options);
        routeLineApi = new MapboxRouteLineApi(options);

        speechApi = new MapboxSpeechApi(MatrixPage.this, getString(R.string.mapbox_access_token), Locale.US.toLanguageTag());
        mapboxVoiceInstructionsPlayer = new MapboxVoiceInstructionsPlayer(MatrixPage.this, Locale.US.toLanguageTag());

        NavigationOptions navigationOptions = new NavigationOptions.Builder(this).accessToken(getString(R.string.mapbox_access_token)).build();

        MapboxNavigationApp.setup(navigationOptions);
        mapboxNavigation = new MapboxNavigation(navigationOptions);

        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver);
        mapboxNavigation.registerRoutesObserver(routesObserver);
        mapboxNavigation.registerLocationObserver(locationObserver);
        mapboxNavigation.registerVoiceInstructionsObserver(voiceInstructionsObserver);

        placeAutocomplete = PlaceAutocomplete.create(getString(R.string.mapbox_access_token));
        searchET = findViewById(R.id.searchET);

        searchResultsView = findViewById(R.id.search_results_view);
        searchResultsView.initialize(new SearchResultsView.Configuration(new CommonSearchViewConfiguration()));

        placeAutocompleteUiAdapter = new PlaceAutocompleteUiAdapter(searchResultsView, placeAutocomplete, LocationEngineProvider.getBestLocationEngine(MatrixPage.this));


        //Access user location from firebase:

        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Replace "userId123" with the actual user ID

        editTextOrigin = findViewById(R.id.editTextOrigin);
        editTextDestination = findViewById(R.id.editTextDestination);
        buttonCalculate = findViewById(R.id.buttonCalculate);
        textViewResult = findViewById(R.id.textViewResult);
        lngLt = findViewById(R.id.lngLnt);
        timeCurrentResult=findViewById(R.id.currentTime);

        //timeCurrentResult.setText("Time: " +  timeDisplay());



        // Read latitude and longitude from the database
        mDatabase.child("userAddresses").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String userId = userSnapshot.getKey(); // Get the user ID
                            Double latitude = userSnapshot.child("latitude").getValue(Double.class);
                            Double longitude = userSnapshot.child("longitude").getValue(Double.class);


                            if (latitude != null && longitude != null) {
                                //System.out.println("Latitude:"+latitude);
                                lngLt.setText("User ıd:"+ userId+"Latitude: " + latitude + ", Longitude: " + longitude);
                            } else {
                                Log.d("MainActivity", "Latitude or Longitude is null for User ID: " + userId);
                            }
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("MatrixPage", "Failed to read value.", databaseError.toException());
                    }
                }
        );

        runnable = new Runnable() {
            @Override
            public void run() {
                // Get the current time
                Date now = new Date();
                // Format the current time
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String formattedTime = timeFormat.format(now);
                // Update the TextView
                timeCurrentResult.setText(formattedTime);
                // Re-run the runnable every second
                handler.postDelayed(this, 1000);
            }
        };

        // Start the runnable for the first time
        handler.post(runnable);

        // Create a list of origin and destination points
        coordinates.add(origin);
        coordinates.add(destination);
        /*
        for(int i = 0 ; i<destinationPoints.size(); i++){
            coordinates.add(destinationPoints.get(i));
        }*/


        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                calculateDistance(coordinates.get(0), coordinates.get(1));

            }
        });


        Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MatrixPage.this, Dashboard.class);
                startActivity(intent);
                finish();
                return;
            }

        });


        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ignoreNextQueryUpdate) {
                    ignoreNextQueryUpdate = false;
                } else {
                    placeAutocompleteUiAdapter.search(charSequence.toString(), new Continuation<Unit>() {
                        @NonNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NonNull Object o) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    searchResultsView.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        MapboxSoundButton soundButton = findViewById(R.id.soundButton);
        soundButton.unmute();
        soundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isVoiceInstructionsMuted = !isVoiceInstructionsMuted;
                if (isVoiceInstructionsMuted) {
                    soundButton.muteAndExtend(1500L);
                    mapboxVoiceInstructionsPlayer.volume(new SpeechVolume(0f));
                } else {
                    soundButton.unmuteAndExtend(1500L);
                    mapboxVoiceInstructionsPlayer.volume(new SpeechVolume(1f));
                }
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(MatrixPage.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (ActivityCompat.checkSelfPermission(MatrixPage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MatrixPage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            activityResultLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            activityResultLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION);
        } else {
            mapboxNavigation.startTripSession();
        }

        focusLocationBtn.hide();
        LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
        getGestures(mapView).addOnMoveListener(onMoveListener);

        setRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MatrixPage.this, "Please select a location in map", Toast.LENGTH_SHORT).show();
            }
        });

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
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_pin);
                AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView);
                PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView);
                addOnMapClickListener(mapView.getMapboxMap(), new OnMapClickListener() {

                    //ALL STATIONS STORED IN  THIS LIST

                    @Override
                    public boolean onMapClick(@NonNull Point point) {
                        destinationPoints.add(point);

                        for (Point destPoint : destinationPoints) {
                            PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                                    .withTextAnchor(TextAnchor.CENTER)
                                    .withIconImage(bitmap)
                                    .withPoint(destPoint);
                            pointAnnotationManager.create(pointAnnotationOptions);
                        }
                        /*
                        pointAnnotationManager.deleteAll();
                        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                                .withPoint(point);
                        pointAnnotationManager.create(pointAnnotationOptions);*/

                        setRoute.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //fetchRoute(point);
                                fetchRoute(destinationPoints);
                            }
                        });
                        return true;
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

                placeAutocompleteUiAdapter.addSearchListener(new PlaceAutocompleteUiAdapter.SearchListener() {
                    @Override
                    public void onSuggestionsShown(@NonNull List<PlaceAutocompleteSuggestion> list) {

                    }

                    @Override
                    public void onSuggestionSelected(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {
                        ignoreNextQueryUpdate = true;
                        focusLocation = false;
                        searchET.setText(placeAutocompleteSuggestion.getName());
                        searchResultsView.setVisibility(View.GONE);

                        pointAnnotationManager.deleteAll();
                        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                                .withPoint(placeAutocompleteSuggestion.getCoordinate());
                        pointAnnotationManager.create(pointAnnotationOptions);
                        updateCamera(placeAutocompleteSuggestion.getCoordinate(), 0.0);

                        setRoute.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchedPoint.add(placeAutocompleteSuggestion.getCoordinate());
                                fetchRoute(searchedPoint);
                            }
                        });
                    }

                    @Override
                    public void onPopulateQueryClick(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {
                        //queryEditText.setText(placeAutocompleteSuggestion.getName());
                    }

                    @Override
                    public void onError(@NonNull Exception e) {

                    }
                });




            }
        });
    }






    private void calculateDistance(Point origin, Point destination) {
        String accessToken = "sk.eyJ1IjoibnVyYmFudWNhbmJheiIsImEiOiJjbHc2NGhuOWUxbDlqMmpwZHB6MThrM2M3In0.f5ZKapBICS8qsCX4S3IDJg";
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

                        // Assuming only one pair of points is requested
                        double distance = (matrixResponse.destinations().get(1).distance())/1000;
                        //double durationInSeconds = matrixResponse.durations().get(0)[0];

                        Location location = new Location("point_to_location");
                        location.setLatitude(origin.latitude());
                        location.setLongitude(origin.longitude());
                        //speed=calculateCurrentSpeed(location)*3.6;
                        speed=5.0;
                        Double time=(distance/speed)*60000.0;;

                        textViewResult.setText("Distance: " + distance + " km\nDuration: " + time +"h" );

                    } else {
                        // Display a message if destinations or durations are empty
                        Toast.makeText(MatrixPage.this, "Empty response for destinations or durations", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Log the error message if the response is not successful
                    Log.e("MatrixAPI", "Failed to get a successful response. Error message: " + response.message());
                    // Display a message if the response is not successful
                    Toast.makeText(MatrixPage.this, "Failed to get a successful response", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MatrixResponse> call, Throwable t) {
                Toast.makeText(MatrixPage.this, "Failed to calculate distance: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void fetchRoute(List<Point> point) {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(MatrixPage.this);
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                Location locations = result.getLastLocation();
                setRoute.setEnabled(false);
                setRoute.setText("Fetching route...");
                RouteOptions.Builder builder = RouteOptions.builder();
                Point origin = Point.fromLngLat(Objects.requireNonNull(locations).getLongitude(), locations.getLatitude());

                List<Point> coord = new ArrayList<>();
                coord.add(origin);
                coord.addAll(point);


                List<Bearing> bearings = new ArrayList<>();
                for (int i = 0; i < coord.size(); i++) {
                    bearings.add(Bearing.builder().angle(locations.getBearing()).degrees(45.0).build());
                }

                List<String> waypointNames = Arrays.asList("Current Location", "Station 1");

                builder.coordinatesList(coord);
                builder.alternatives(true);
                //builder.waypointNamesList(waypointNames);
                builder.profile(DirectionsCriteria.PROFILE_DRIVING);
                builder.bearingsList(bearings);

                applyDefaultNavigationOptions(builder);

                mapboxNavigation.requestRoutes(builder.build(), new NavigationRouterCallback() {
                    @Override
                    public void onRoutesReady(@NonNull List<NavigationRoute> list, @NonNull RouterOrigin routerOrigin) {
                        mapboxNavigation.setNavigationRoutes(list);
                        focusLocationBtn.performClick();
                        setRoute.setEnabled(true);
                        setRoute.setText("Set route");
                    }

                    @Override
                    public void onFailure(@NonNull List<RouterFailure> list, @NonNull RouteOptions routeOptions) {
                        setRoute.setEnabled(true);
                        setRoute.setText("Set route");
                        Toast.makeText(MatrixPage.this, "Route request failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCanceled(@NonNull RouteOptions routeOptions, @NonNull RouterOrigin routerOrigin) {

                    }
                });
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
}