#include <math.h>
#include <uWS/uWS.h>
#include <iostream>
#include <string>
#include "json.hpp"
#include "PID.h"

// for convenience
using nlohmann::json;
using std::string;

// For converting back and forth between radians and degrees.
constexpr double pi() { return M_PI; }
double deg2rad(double x) { return x * pi() / 180; }
double rad2deg(double x) { return x * 180 / pi(); }

// Checks if the SocketIO event has JSON data.
// If there is data the JSON object in string format will be returned,
// else the empty string "" will be returned.
string hasData(string s)
{
  auto found_null = s.find("null");
  auto b1 = s.find_first_of("[");
  auto b2 = s.find_last_of("]");
  if (found_null != string::npos)
  {
    return "";
  }
  else if (b1 != string::npos && b2 != string::npos)
  {
    return s.substr(b1, b2 - b1 + 1);
  }
  return "";
}

int main()
{
  uWS::Hub h;

  PID pid;
  /**
   * TODO: Initialize the pid variable.
   */
  double prev_cte = 0.0;
  double int_cte = 0.0;
  pid.Init(0.2, 0.0, 3);

  h.onMessage([&pid, &prev_cte, &int_cte](uWS::WebSocket<uWS::SERVER> ws, char *data, size_t length,
                                          uWS::OpCode opCode) {
    // "42" at the start of the message means there's a websocket message event.
    // The 4 signifies a websocket message
    // The 2 signifies a websocket event
    if (length && length > 2 && data[0] == '4' && data[1] == '2')
    {
      auto s = hasData(string(data).substr(0, length));

      if (s != "")
      {
        auto j = json::parse(s);

        string event = j[0].get<string>();

        if (event == "telemetry")
        {
          // j[1] is the data JSON object
          double cte = std::stod(j[1]["cte"].get<string>());
          double speed = std::stod(j[1]["speed"].get<string>());
          double angle = std::stod(j[1]["steering_angle"].get<string>());
          double steer_value;
          /**
           * TODO: Calculate steering value here, remember the steering value is
           *   [-1, 1].
           * NOTE: Feel free to play around with the throttle and speed.
           *   Maybe use another PID controller to control the speed!
           */

          //  Twiddle parameters for PID
          bool fullspeed = false;
          double tol = 0.001; //  Need fine tune
          if (pid.TotalError() > tol)
          {
            pid.UpdateError(cte);
          }
          else
          {
            fullspeed = true;
          }

          double Kp = pid.GetKp();
          double Ki = pid.GetKi();
          double Kd = pid.GetKd();

          //  PID Controller
          double diff_cte = cte - prev_cte;
          prev_cte = cte;
          int_cte += cte;

          steer_value = -Kp * cte - Kd * diff_cte - Ki * int_cte;

          //  Throttle control
          double throttle = 0.4;

          if (fullspeed == false) //  For twiddle tuning
          {
            if (speed >= 20)
            {
              throttle = 0;
            }
          }
          else
          {
            if (speed >= 40)  //  Speed limit to 40MPH
            {
              throttle = 0.0;
            }
            if (abs(cte) > 1.0 && speed > 20) //  If the car is off the center by 1, reduce speed
            {
              throttle = -0.15;
            }
          }
          

          // DEBUG
          // std::cout << "CTE: " << cte << " Steering Value: " << steer_value
          //           << std::endl;
          std::cout << "Kp: " << Kp << " | Kd:" << Kd << " | Ki:" << Ki << " | TotalError: " << pid.TotalError() << " | CTE: " << cte <<  std::endl;

          json msgJson;
          msgJson["steering_angle"] = steer_value;
          msgJson["throttle"] = throttle;
          auto msg = "42[\"steer\"," + msgJson.dump() + "]";
          // std::cout << msg << std::endl;
          ws.send(msg.data(), msg.length(), uWS::OpCode::TEXT);
        } // end "telemetry" if
      }
      else
      {
        // Manual driving
        string msg = "42[\"manual\",{}]";
        ws.send(msg.data(), msg.length(), uWS::OpCode::TEXT);
      }
    } // end websocket message if
  }); // end h.onMessage

  h.onConnection([&h](uWS::WebSocket<uWS::SERVER> ws, uWS::HttpRequest req) {
    std::cout << "Connected!!!" << std::endl;
  });

  h.onDisconnection([&h](uWS::WebSocket<uWS::SERVER> ws, int code,
                         char *message, size_t length) {
    ws.close();
    std::cout << "Disconnected" << std::endl;
  });

  int port = 4567;
  if (h.listen(port))
  {
    std::cout << "Listening to port " << port << std::endl;
  }
  else
  {
    std::cerr << "Failed to listen to port" << std::endl;
    return -1;
  }

  h.run();
}