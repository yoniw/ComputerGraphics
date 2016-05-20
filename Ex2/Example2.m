H = [1 .2 0; .1 1 0; 0.5 0.2 1];
dist_ratio = 0.6;

im1 = imread('lena.bmp');
im2 = ComputeProjective(im1, H);

[num_matches, matches, dist_vals] = match(im1, im2, dist_ratio);

% Calculate error using DLT.
dlt_computed = DLT(matches');

% Calculate error using RANSAC.
num_samples = 4;
threshold = 100;
is_feedback = 0;
max_degen_attempts = 1;
max_iterations = 100000;

ransac_computed = RANSAC_Wrapper(matches', @fittingfn, @distfn, @degenfn, num_samples, threshold, is_feedback, max_degen_attempts, max_iterations);

% Show results.
disp('DLT error: ');
disp(ComputeError(H, dlt_computed));
disp('RANSAC error: ');
disp(ComputeError(H, ransac_computed));