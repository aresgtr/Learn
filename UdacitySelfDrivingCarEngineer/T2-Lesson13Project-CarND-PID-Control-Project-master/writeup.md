# CarND-Controls-PID
Self-Driving Car Engineer Nanodegree Program

---

The goal of this project are the following:
* Build a PID controller and tune the PID hyperparameters by Twiddle Algorithm in C++
* Test the PID controller on the simulator to drive the car around the track

Here is an example image of this project:

![alt text][image_example]

Please refer to /src/ for source code.

### Simulator

The simulator for this project is provided by Udacity from the [project intro page](https://github.com/udacity/self-driving-car-sim/releases).

[//]: # (Image References)

[image_example]: ./writeup_images/example.jpg "Example"

## Details

### PID Controller

PID Controller is described as follows:
```python
steering = -tau_p * CTE - tau_d * diff_CTE - tau_i * int_CTE
```
To set a proper steering angle for the car, we need to turn the car in proportion to the cross track error, and this is how `tau_p` works. However, overshoot happens for single P control. To overcome this, tau_d is introduced to reduce oscillations. If there is a bias in car's tuning, i.e. the car tend to steer to one direction to another, I variable is introduced. `int_CTE` is the incremented cte as times goes, and `tau_i` is for compensating the bias.

In this project, all three variables are calculated by twiddle algorithm.

### Twiddle Algorithm

Twiddle alforithm has been implemented in this project. The example below is an implementation of twiddle for p parameter for PID.

```c++
if (abs(cte) < abs(best_error) - 0.05)
{
  best_error = cte;
  p_error *= 1.01;
  Kp += p_error;
}
else
{
  if (update_counter_p == 0)
  {
    Kp -= 2 * p_error;
    update_counter_p++;
  }
  else
  {
    Kp += p_error;
    p_error *= 0.99;
    Kp += p_error;
    update_counter_p = 0;

  }
}
```

I made 0.05 as the threshold for tuning the PID. Since the track is curvy and difficult for tuning, if I remove the threshold value, the program will tune infinitely because `cte` is almost always lower than `best_error` by a very small number. Thus, I put a threshold value to make sure p value is updated only if it need to be updated.

For the tuning part, I sat the speed for the car to be 20. I found it is easier to tune by this speed. Tuning will last for about a lap until the total error is less than 0.001.

It turns out that the PID tuning gives me parameters of following:

Kp: 0.200336 | Kd:2.00066 | Ki:-9.99329e-08 | TotalError: 0.000999904 | CTE: 0.3799

The parameters are well enough for the car to drive roughly around speed of 30MPH. After tuning, the car automatically accelerate to 40MPH.

### Throttle Control
Throttle is not implemented by PID for now, but I implemented a easy algorithm. The car will accelerate to 40MPH and maintain the speed. However when `cte` is grater than 1, it will press the brake and slow down the car, which makes it easier for PID to adjust.

## Reflection

The car drives relatively well and not really bouncing most of the time, but when going through the sharp turns, it brakes down the speed a lot. This makes this project slightly unpleasant to watch. To make it better, better algorithm for PID tuning need to be implemented. In the future, PID control for the throttle will also be implemented for a smoother ride.