# CarND-Path-Planning-Project
Self-Driving Car Engineer Nanodegree Program

The goal of this project are the following:
* Safely navigate around a virtual highway with other traffic that is driving +-10 MPH of the 50 MPH speed limit
* Car's localization and sensor fusion data, as well as map waypoints data are provided
* The car should try to go as close as possible to the 50 MPH speed limit
* When there is a slower traffic, the car should be able to change lanes if safe to do so
* The car should avoid hitting other cars and keep in the lane
* The car should not experience total acceleration over 10 m/s^2 and jerk that is greater than 10 m/s^3

The image below is an example of the project:

<img src="./writeup_images/example.jpg" width="600">

The project is written by C++. The code for this project are under the `src` directory.
   
### Simulator.
The simulator for this project is provided by Udacity from the [releases tab (https://github.com/udacity/self-driving-car-sim/releases/tag/T3_v1.2).  


## Details

### Car detection

Sensor fusion is key to safe driving. The strategy that I took was:

First, mark the lanes of all the other cars around us detected by sensor fusion. We need to know if they are in our lane, our left, or our right.

```c++
float d = sensor_fusion[i][6];  //  Sensor fusion data
if (d > 0 && d <= 4)
{
  //  Left lane
  car_lane = 0;
}
else if (d > 4 && d <= 8)
{
  //  Middle lane
  car_lane = 1;
}
else if (d > 8)
{
  //  Right lane
  car_lane = 2;
}
```

Then, I made three variables `too_close`, `left_too_close` and `right_too_close` to represent if the other cars are too close to us. `left_too_close` and `right_too_close` are indications of the result of "shoulder check".

Then, we perform lane change only if there is a slow car in front of us, and we have a safe gap beside us. Otherwise, we will reduce speed and follow the traffic.

Because of the if statements below, the order matters. We have a bias of tend to change to left lane than right lane.

```c++
if (too_close)
{
  if (lane == 1 && !left_too_close) // We are in middle lane
  {
    lane--;
  }
  else if (lane == 1 && !right_too_close)
  {
    lane++;
  }

  else if (lane == 0 && !right_too_close) //  We are in left lane
  {
    lane++;
  }
  else if (lane == 2 && !left_too_close) //  We are in right lane
  {
    lane--;
  }
  else
  {
    ref_vel -= MAX_ACC;
  }
}
else if (ref_vel < MAX_SPEED)
{
  ref_vel += MAX_ACC;
}
```

This is an example of a safe lane change.

<img src="./writeup_images/lane_change.jpg" width="600">

This is an example of following the traffic. There is no safe gap for lane change.

<img src="./writeup_images/no_lane_change.jpg" width="600">

### Trajectory generation

Spline libarary is used for making the driving lane smooth. We create far points and use a spline to fit the points, and make points by the spline.

## Reflection

The project has been running reliably. The car is driving well and safe. However, although all the rubricks are met, there are lots of improvements.

For instance, if there is a slow car infront of us and there is no safe gap for us to change lane, we should follow the slow traffic. In the real world, adaptive cruise control systems are designed to match the same speed as the traffic goes. However in the project, our car slows down quite a bit and accelerate to keep up the distance, and then when it gets too close, it slows down again. This creates uncomfortable driving experience for the passengers. To solve this problem, acceleration and braking variables need to be fine-tuned. We could also implement a feedback system to adjust the speed to reduce the damp.

Furthermore, there are some edge cases like there is a car in the center lane beside us while we are at the right lane. The right lane goes slow, but we could not make a safe lane change because of the car on our left. If the left most lane is almost empty, in the real world we are more likely trying to slow down more to create a safe gap (by letting the middle lane car overtake us), and change our lane to the very left. This type of cases are not implemented by this project.


