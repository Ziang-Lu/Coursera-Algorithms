/*
Problem Description:
    Given an array of integer p[], find the number of inversions in it.
    A inversion can be defined as p[i]>p[j] && i<j
    Naive implementation: loop through all combination, O(N^2)
    My implementation: divide and conquer, similar to merge sort
        Find the number of inversion in left, find the number of inversion in right
        merge sort left and right, and add the number of inversion between left and right.
        O(NlogN)  
*/
#include <iostream>
#include <stdlib.h>
#include <time.h>
void inversion(int p[], int l, int r, int & num_of_inversion)
{
    if (r - l != 0)
    {
        int m = (int)((l + r)/2);
        inversion(p, l, m, num_of_inversion);
        inversion(p, m+1, r, num_of_inversion);
        int left_length = m - l + 1;
        int right_length = r - m;
        int temp_left[left_length];
        int temp_right[right_length];
        for (int i = 0; i < left_length; i++)
        {
            temp_left[i] = p[l+i];
        }
        for (int i = 0; i < right_length; i++)
        {
            temp_right[i] = p[m+1+i];
        }
        int i = 0, j = 0, k = l;
        while (i < left_length && j < right_length)
        {
            if (temp_left[i] <= temp_right[j])
            {
                p[k] = temp_left[i];
                i += 1;
                k += 1;
                num_of_inversion += j;
            }
            else
            {
                p[k] = temp_right[j];
                j += 1;
                k += 1;
            }
        }

        while (i < left_length)
        {
            p[k] = temp_left[i];
            i += 1;
            k += 1;
            num_of_inversion += j;
        }
        while (j < right_length)
        {
            p[k] = temp_right[j];
            j += 1;
            k += 1;
        }
    }
    return;
}

int main()
{
    srand(time(NULL));
    int size = 6;
    int p[size];
    for (int i = 0; i < size; i++)
    {
        p[i] = rand()%10;
    }
    for (int i = 0; i < size; i++)
    {
        std::cout<< p[i] <<" "; 
    }
    std::cout<<std::endl;

    int num_of_inversion = 0;
    inversion(p, 0, size-1, num_of_inversion);
    std::cout << "number of inversion is: "<<num_of_inversion<<std::endl;
    return 0;
}