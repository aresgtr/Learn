/**
 * particle_filter.cpp
 *
 * Created on: Dec 12, 2016
 * Author: Tiffany Huang
 */

#include "particle_filter.h"

#include <math.h>
#include <algorithm>
#include <iostream>
#include <iterator>
#include <numeric>
#include <random>
#include <string>
#include <vector>

#include "helper_functions.h"

using std::string;
using std::vector;

void ParticleFilter::init(double x, double y, double theta, double std[]) {
  /**
   * TODO: Set the number of particles. Initialize all particles to 
   *   first position (based on estimates of x, y, theta and their uncertainties
   *   from GPS) and all weights to 1. 
   * TODO: Add random Gaussian noise to each particle.
   * NOTE: Consult particle_filter.h for more information about this method 
   *   (and others in this file).
   */
  num_particles = 1000;  // TODO: Set the number of particles
  particles = vector<Particle>(num_particles);
  std::default_random_engine gen;

  //  Set standard deviations
  double std_x, std_y, std_theta;
  std_x = std[0];
  std_y = std[1];
  std_theta = std[2];

  //  Create normal distribution for x, y, and theta
  std::normal_distribution<double> dist_x(x, std_x);
  std::normal_distribution<double> dist_y(y, std_y);
  std::normal_distribution<double> dist_theta(theta, std_theta);

  for (int i = 0; i < num_particles; ++i) {
    Particle particle;
    //  sample from these normal distribution
    particle.id = i;
    particle.x = dist_x(gen);
    particle.y = dist_y(gen);
    particle.theta = dist_theta(gen); 
    particle.weight = 1;
    particles[i] = particle;
  }
  weights = vector<double >(num_particles, 1);
}

void ParticleFilter::prediction(double delta_t, double std_pos[], 
                                double velocity, double yaw_rate) {
  /**
   * TODO: Add measurements to each particle and add random Gaussian noise.
   * NOTE: When adding noise you may find std::normal_distribution 
   *   and std::default_random_engine useful.
   *  http://en.cppreference.com/w/cpp/numeric/random/normal_distribution
   *  http://www.cplusplus.com/reference/random/default_random_engine/
   */

  //  Set standard deviations
  double std_x, std_y, std_theta;
  std_x = std_pos[0];
  std_y = std_pos[1];
  std_theta = std_pos[2];

  double new_x;
  double new_y;
  double new_theta;

  for (int i = 0; i < num_particles; ++i) {
    //  if yaw_rate is 0
    if (yaw_rate == 0) {
      new_x = particles[i].x + velocity * cos(particles[i].theta) * delta_t;
      new_y = particles[i].y + velocity * sin(particles[i].theta) * delta_t;
      new_theta = particles[i].theta;
    }
    //  if yaw_rate is not 0
    else {
      new_x = particles[i].x + (velocity/yaw_rate) * (sin(particles[i].theta + (yaw_rate * delta_t)) - sin(particles[i].theta));
      new_y = particles[i].y + (velocity/yaw_rate) * (cos(particles[i].theta) - cos(particles[i].theta + (yaw_rate * delta_t)));
      new_theta = particles[i].theta + (yaw_rate * delta_t);
    }

    std::normal_distribution<double> dist_x(new_x, std_x);
    std::normal_distribution<double> dist_y(new_y, std_y);
    std::normal_distribution<double> dist_theta(new_theta, std_theta);

    std::default_random_engine gen;

    particles[i].x = dist_x(gen);
    particles[i].y = dist_y(gen);
    particles[i].theta = dist_theta(gen);
  }
}

void ParticleFilter::dataAssociation(vector<LandmarkObs> predicted, 
                                     vector<LandmarkObs>& observations) {
  /**
   * TODO: Find the predicted measurement that is closest to each 
   *   observed measurement and assign the observed measurement to this 
   *   particular landmark.
   * NOTE: this method will NOT be called by the grading code. But you will 
   *   probably find it useful to implement this method and use it as a helper 
   *   during the updateWeights phase.
   */

  for (int i = 0; i < observations.size(); ++i) {

    double min_distance = 99999.0;
    int match_id;

    for (int j = 0; j < predicted.size(); ++j) {
      double distance;
      distance = dist(observations[i].x, observations[i].y, predicted[j].x, predicted[j].y);

      if (distance < min_distance) {
        min_distance = distance;
        match_id = predicted[j].id;
      }
    }
    observations[i].id = match_id;

  }
}

void ParticleFilter::updateWeights(double sensor_range, double std_landmark[], 
                                   const vector<LandmarkObs> &observations, 
                                   const Map &map_landmarks) {
  /**
   * TODO: Update the weights of each particle using a mult-variate Gaussian 
   *   distribution. You can read more about this distribution here: 
   *   https://en.wikipedia.org/wiki/Multivariate_normal_distribution
   * NOTE: The observations are given in the VEHICLE'S coordinate system. 
   *   Your particles are located according to the MAP'S coordinate system. 
   *   You will need to transform between the two systems. Keep in mind that
   *   this transformation requires both rotation AND translation (but no scaling).
   *   The following is a good resource for the theory:
   *   https://www.willamette.edu/~gorr/classes/GeneralGraphics/Transforms/transforms2d.htm
   *   and the following is a good resource for the actual equation to implement
   *   (look at equation 3.33) http://planning.cs.uiuc.edu/node99.html
   */

  for (int i = 0; i < num_particles; ++i) {

    //  transform observations from vehicle coordinate to global coordinate
    vector<LandmarkObs> global_observations;
    for (unsigned int j = 0; j < observations.size(); ++j) {
      LandmarkObs global_observation;
      global_observation.x = observations[j].x * cos(particles[i].theta) - observations[j].y * sin(particles[i].theta) + particles[i].x;
      global_observation.y = observations[j].x * sin(particles[i].theta) + observations[j].y * cos(particles[i].theta) + particles[i].y;
      global_observation.id = observations[j].id;
      global_observations.push_back(global_observation);
    }

    //  see if the inputs are in the sensor range
    vector<LandmarkObs> in_range;
    for (unsigned int j = 0; j < map_landmarks.landmark_list.size(); j++) {
      double distance;
      distance = dist(particles[i].x, particles[i].y, map_landmarks.landmark_list[j].x_f, map_landmarks.landmark_list[j].y_f);

      if (distance <= sensor_range) {
        LandmarkObs in_range_landmark;
        in_range_landmark.x = map_landmarks.landmark_list[j].x_f;
        in_range_landmark.y = map_landmarks.landmark_list[j].y_f;
        in_range_landmark.id = map_landmarks.landmark_list[j].id_i;

        in_range.push_back(in_range_landmark);
      }
    }

    //  associate global observations to predicted in-range landmarks
    dataAssociation(in_range, global_observations);

    //  reset weight
    
    for (unsigned int j = 0; j < in_range.size(); j++) {
      double weight = 1;
      // find the nearest
      double min_distance = 99999.0;
      int match_id;

      for (int k = 0; k < global_observations.size(); k++) {
        double distance;
        distance = dist(in_range[j].x, in_range[j].y, global_observations[k].x, global_observations[k].y);

        if (distance < min_distance)
        {
          min_distance = distance;
          match_id = k;
        }

        

      }
      
      // weight *= bivariate_normal(in_range[j].x, in_range[j].y, global_observations[match_id].x, global_observations[match_id].y, std_landmark[0], std_landmark[1]);
      weight *= exp(-((in_range[j].x-global_observations[match_id].x)*(in_range[j].x-global_observations[match_id].x)/(2*std_landmark[0]*std_landmark[0]) + (in_range[j].y-global_observations[match_id].y)*(in_range[j].y-global_observations[match_id].y)/(2*std_landmark[1]*std_landmark[1]))) / (2.0*3.14159*std_landmark[0]*std_landmark[1]);
      weights.push_back(weight);
      particles[i].weight = weight;
    }
    
  }

}

void ParticleFilter::resample() {
  /**
   * TODO: Resample particles with replacement with probability proportional 
   *   to their weight. 
   * NOTE: You may find std::discrete_distribution helpful here.
   *   http://en.cppreference.com/w/cpp/numeric/random/discrete_distribution
   */

  vector<Particle> resampled;
  std::default_random_engine gen;
  
  std::uniform_int_distribution<int> index(0, num_particles - 1);
  int current_index = index(gen);
  double b = 0;

  double max_weight = *max_element(weights.begin(), weights.end());

  for (int i = 0; i < num_particles; i++) {
    std::uniform_real_distribution<double> random_weight(0.0, max_weight * 2);
    b = random_weight(gen);

    while (b > weights[current_index])
    {
      b = b - weights[current_index];
      current_index = (current_index + 1) % num_particles;
    }
    resampled.push_back(particles[current_index]);
  }
  particles = resampled;

}

void ParticleFilter::SetAssociations(Particle& particle, 
                                     const vector<int>& associations, 
                                     const vector<double>& sense_x, 
                                     const vector<double>& sense_y) {
  // particle: the particle to which assign each listed association, 
  //   and association's (x,y) world coordinates mapping
  // associations: The landmark id that goes along with each listed association
  // sense_x: the associations x mapping already converted to world coordinates
  // sense_y: the associations y mapping already converted to world coordinates
  particle.associations= associations;
  particle.sense_x = sense_x;
  particle.sense_y = sense_y;
}

string ParticleFilter::getAssociations(Particle best) {
  vector<int> v = best.associations;
  std::stringstream ss;
  copy(v.begin(), v.end(), std::ostream_iterator<int>(ss, " "));
  string s = ss.str();
  s = s.substr(0, s.length()-1);  // get rid of the trailing space
  return s;
}

string ParticleFilter::getSenseCoord(Particle best, string coord) {
  vector<double> v;

  if (coord == "X") {
    v = best.sense_x;
  } else {
    v = best.sense_y;
  }

  std::stringstream ss;
  copy(v.begin(), v.end(), std::ostream_iterator<float>(ss, " "));
  string s = ss.str();
  s = s.substr(0, s.length()-1);  // get rid of the trailing space
  return s;
}