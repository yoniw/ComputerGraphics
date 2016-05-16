function [num_matches, matches, dist_vals] = match(image1, image2, distRatio)
%UNTITLED Summary of this function goes here
%   Detailed explanation goes here

	% Find SIFT keypoints for each image
	[im1, des1, loc1] = sift(image1);
	[im2, des2, loc2] = sift(image2);

    % num_matches = 0;
    matches = [];
	dist_vals = [];
	% For each descriptor in the first image, select its match to second image.
	des2t = des2';                          % Precompute matrix transpose
	for i = 1 : size(des1,1)
	   dotprods = des1(i,:) * des2t;        % Computes vector of dot products
	   [vals,indx] = sort(acos(dotprods));  % Take inverse cosine and sort results

	   % Check if nearest neighbor has angle less than distRatio times 2nd.
	   if (vals(1) < distRatio * vals(2))
	      % match(i) = indx(1);
	      dist_vals = [dist_vals; vals(1)];
          % match = [loc1(i, 1:2), loc2(indx(1), 1:2)];
          match = [fliplr(loc1(i, 1:2)), fliplr(loc2(indx(1), 1:2))];
          matches = [matches; match];
          % num_matches = num_matches + 1;
	   else
	      % match(i) = 0;
	   end
	end

	% num_matches = sum(match > 0);
    num_matches = size(matches, 1);
end