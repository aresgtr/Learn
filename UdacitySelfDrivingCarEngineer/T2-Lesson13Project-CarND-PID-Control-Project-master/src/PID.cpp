#include "PID.h"
#include <iostream>

/**
 * TODO: Complete the PID class. You may add any additional desired functions.
 */

PID::PID() {}

PID::~PID() {}

void PID::Init(double Kp_, double Ki_, double Kd_)
{
  /**
   * TODO: Initialize PID coefficients (and errors, if needed)
   */
  // Kp = 0.2;
  // Kd = 3.0;
  // Ki = 0.0;
  Kp = Kp_;
  Ki = Ki_;
  Kd = Kd_;

  p_error = 0.5;
  i_error = 0.0000001;
  d_error = 1.0;

  best_error = 999.0; //  A very large double
  update_counter_p = 0;
  update_counter_d = 0;
  update_counter_i = 0;
  update_switch = 0;
}

void PID::UpdateError(double cte)
{
  /**
   * TODO: Update PID errors based on cte.
   */

  std::cout << "BestError: " << best_error << std::endl;

  if (update_switch == 0)
  {
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

        update_switch = 1;
      }
    }
  }
  else if (update_switch == 1)
  {
    if (abs(cte) < abs(best_error) - 0.05)
    {
      best_error = cte;
      d_error *= 1.01;
      Kd += d_error;
    }
    else
    {
      if (update_counter_d == 0)
      {
        Kd -= 2 * d_error;
        update_counter_d++;
      }
      else
      {
        Kd += d_error;
        d_error *= 0.99;
        Kd += d_error;
        update_counter_d = 0;

        update_switch = 2;
      }
    }
  }
  else if (update_switch == 2)
  {
    if (abs(cte) < abs(best_error) - 0.05)
    {
      best_error = cte;
      i_error *= 1.01;
      Ki += i_error;
    }
    else
    {
      if (update_counter_i == 0)
      {
        Ki -= 2 * i_error;
        update_counter_i++;
      }
      else
      {
        Ki += i_error;
        i_error *= 0.99;
        Ki += i_error;
        update_counter_i = 0;

        update_switch = 0;
      }
    }
  }
}

double PID::TotalError()
{
  /**
   * TODO: Calculate and return the total error
   */
  return p_error + i_error + d_error; // TODO: Add your total error calc here!
}

double PID::GetKp()
{
  return Kp;
}

double PID::GetKd()
{
  return Kd;
}

double PID::GetKi()
{
  return Ki;
}
