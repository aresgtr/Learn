#include <iostream>
#include <vector>

using std::vector;

// initialize priors assuming vehicle at landmark +/- 1.0 meters position stdev
vector<float> initialize_priors(int map_size, vector<float> landmark_positions,
    float position_stdev);

int main() {
    // set standard deviation of position
    float position_stdev = 1.0f;

    // set map horizon distance in meters 
    int map_size = 25;

    // initialize landmarks
    vector<float> landmark_positions{ 5, 10, 20 };

    // initialize priors
    vector<float> priors = initialize_priors(map_size, landmark_positions,
        position_stdev);

    // print values to stdout 
    for (int p = 0; p < priors.size(); ++p) {
        std::cout << priors[p] << std::endl;
    }

    return 0;
}

// TODO: Complete the initialize_priors function
vector<float> initialize_priors(int map_size, vector<float> landmark_positions,
    float position_stdev) {

    // initialize priors assuming vehicle at landmark +/- 1.0 meters position stdev

    // set all priors to 0.0
    vector<float> priors(map_size, 0.0);

    // TODO: YOUR CODE HERE
    float norm_term = landmark_positions.size() * (position_stdev * 2 + 1);
    float weight = 1 / norm_term;

    for (int i = 0; i < landmark_positions.size(); ++i) {
        for (int index = 1; index <= map_size; ++index) {
            if (abs(landmark_positions[i] - index) <= position_stdev) {
                priors.at(index) = weight;
            }
        }
    }

    // One working solution
    /*for (int i = 0; i < landmark_positions.size(); ++i) {
        for (float j = 1; j <= position_stdev; ++j) {
            priors.at(int(j + landmark_positions[i] + map_size) % map_size) += 1.0 / norm_term;
            priors.at(int(-j + landmark_positions[i] + map_size) % map_size) += 1.0 / norm_term;
        }
        priors.at(landmark_positions[i]) += 1.0 / norm_term;
    }*/

    return priors;
}