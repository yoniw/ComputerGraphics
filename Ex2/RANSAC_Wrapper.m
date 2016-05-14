function [ H_ransac ] = RANSAC_Wrapper(matches, fittingfn, distfn, degenfn, s, t, feedback, maxDataTrials, maxTrials)
%RANSAC_WRAPPER Summary of this function goes here
%   Detailed explanation goes here
    [H_ransac, ~] = ransac(matches, fittingfn, distfn, degenfn, s, t, feedback, maxDataTrials, maxTrials);

end

