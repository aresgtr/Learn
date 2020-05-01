//OBS1 is the sensor observation reported from the sensor. As noted in the figure, OBS1 is (2,2). What is the position of OBS1 in map coordinates (x_map,y_map)? Enter your answer in parenthesis with the x value separated from the y value with a comma and no spaces.

#include <cmath>
#include <iostream>

int main() {
	//	define coordinates and theta
	double x_part, y_part, x_obs, y_obs, theta;
	//	车位于全局坐标的位置
	x_part = 4;
	y_part = 5;
	//	车自己往前走的量
	x_obs = 2;
	y_obs = 2;
	theta = -3.1415926 / 2; // -90 degrees

	//	transform to map x coordinate
	double x_map;
	x_map = x_part + (cos(theta) * x_obs) - (sin(theta) * y_obs);

	//	transform to map y coordinate
	double y_map;
	y_map = y_part + (sin(theta) * x_obs) + (cos(theta) * y_obs);

	//	(6, 3)
	std::cout << int(round(x_map)) << ", " << int(round(y_map)) << std::endl;

	return 0;
}