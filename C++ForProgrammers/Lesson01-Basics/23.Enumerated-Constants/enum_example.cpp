/*Enum example*/

#include <iostream>

using namespace std;

int main()
{
    //  Define MONTHS as having 12 possible values
    enum MONTHS {Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec};

    //  Define bestMonth as a variable type MONTHS
    MONTHS bestMonth;

    //  Assign bestMonth one of the values of MONTHS
    bestMonth = Jan;

    //  Now we can check the value of bestMonths just like any other variable
    if (bestMonth == Jan)
    {
        cout << "I'm not so sure January is the best month\n";
    }
    return 0;
}
