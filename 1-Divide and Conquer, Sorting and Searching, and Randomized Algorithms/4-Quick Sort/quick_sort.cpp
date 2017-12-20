#include <iostream>
#include <stdlib.h>
#include <time.h>
void swap(int* a, int* b)
{
    int temp = *a;
    *a = *b;
    *b = temp;
}

int partition(int arr[], int l, int r)
{
    int pivot = arr[r];
    int i = l - 1;

    for (int j = l; j < r; j++)
    {
        if (arr[j] < pivot)
        {
            i += 1;
            swap(arr+i, arr+j);
        }
    }
    swap(arr+i+1, arr+r);
    return i+1;
}

void quick_sort(int arr[], int l, int r)
{
    if (l < r)
    {
        int m = partition(arr, l, r);
        quick_sort(arr, l, m-1);
        quick_sort(arr, m+1,r);
    }
}


int main()
{
    
    srand(time(NULL));
    int size = 6;
    int arr[size];
    for (int i = 0; i < size; i++)
    {
        arr[i] = rand()%10;
    }
    std::cout<<"before sorting: ";
    for (int i = 0; i < size; i++)
    {
        std::cout<<arr[i]<<" ";
    }
    std::cout<<std::endl;

    quick_sort(arr, 0, size-1);    // call quick sort here

    std::cout<<"after sorting: ";
    for (int i = 0; i < size; i++)
    {
        std::cout<<arr[i]<<" ";
    }
    std::cout<<std::endl;

    return 0;
}