H = [1 .2 0; .1 1 0; 0.5 0.2 1];
dist_ratio = 0.6;

im1 = imread('lena.bmp');
im2 = ComputeProjective(im1, H);

[num_matches, matches, dist_vals] = match(im1, im2, dist_ratio);

% Calculate error using DLT.
num_iterations = 10;
err_sum = 0;
for i = 1 : num_iterations
    dlt_computed = DLT(matches');
    err_sum = err_sum + ComputeError(H, dlt_computed);
end

% Calculate error using RANSAC.
num_samples = 5;
threshold = 100;
is_feedback = 0;
max_degen_attempts = 100;
max_iterations = 1000;

ransac_computed = RANSAC_Wrapper(matches, @fittingfn, @distfn, @degenfn, num_samples, threshold, is_feedback, max_degen_attempts, max_iterations);